/*
 * This file is part of the OpenSCADA project
 * Copyright (C) 2006-2009 TH4 SYSTEMS GmbH (http://th4-systems.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.openscada.opc.dcom.da;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JIProgId;
import org.jinterop.dcom.core.JISession;
import org.openscada.opc.dcom.common.EventHandler;
import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.Result;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.common.impl.OPCCommon;
import org.openscada.opc.dcom.da.impl.OPCAsyncIO2;
import org.openscada.opc.dcom.da.impl.OPCGroupStateMgt;
import org.openscada.opc.dcom.da.impl.OPCItemMgt;
import org.openscada.opc.dcom.da.impl.OPCServer;
import org.openscada.opc.dcom.da.impl.OPCAsyncIO2.AsyncResult;

public class Test2
{
    private static JISession _session = null;

    public static void showError ( final OPCCommon common, final int errorCode ) throws JIException
    {
        System.out.println ( String.format ( "Error (%X): '%s'", errorCode, common.getErrorString ( errorCode, 1033 ) ) );
    }

    public static void showError ( final OPCServer server, final int errorCode ) throws JIException
    {
        showError ( server.getCommon (), errorCode );
    }

    public static boolean dumpOPCITEMRESULT ( final KeyedResultSet<OPCITEMDEF, OPCITEMRESULT> result )
    {
        int failed = 0;
        for ( final KeyedResult<OPCITEMDEF, OPCITEMRESULT> resultEntry : result )
        {
            System.out.println ( "==================================" );
            System.out.println ( String.format ( "Item: '%s' ", resultEntry.getKey ().getItemID () ) );

            System.out.println ( String.format ( "Error Code: %08x", resultEntry.getErrorCode () ) );
            if ( !resultEntry.isFailed () )
            {
                System.out.println ( String.format ( "Server Handle: %08X", resultEntry.getValue ().getServerHandle () ) );
                System.out.println ( String.format ( "Data Type: %d", resultEntry.getValue ().getCanonicalDataType () ) );
                System.out.println ( String.format ( "Access Rights: %d", resultEntry.getValue ().getAccessRights () ) );
                System.out.println ( String.format ( "Reserved: %d", resultEntry.getValue ().getReserved () ) );
            }
            else
            {
                failed++;
            }
        }
        return failed == 0;
    }

    public static void testItems ( final OPCServer server, final OPCGroupStateMgt group, final String... itemIDs ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        final OPCItemMgt itemManagement = group.getItemManagement ();
        final List<OPCITEMDEF> items = new ArrayList<OPCITEMDEF> ( itemIDs.length );
        for ( final String id : itemIDs )
        {
            final OPCITEMDEF item = new OPCITEMDEF ();
            item.setItemID ( id );
            item.setClientHandle ( new Random ().nextInt () );
            items.add ( item );
        }

        final OPCITEMDEF[] itemArray = items.toArray ( new OPCITEMDEF[0] );

        System.out.println ( "Validate" );
        KeyedResultSet<OPCITEMDEF, OPCITEMRESULT> result = itemManagement.validate ( itemArray );
        if ( !dumpOPCITEMRESULT ( result ) )
        {
            return;
        }

        // now add them to the group
        System.out.println ( "Add" );
        result = itemManagement.add ( itemArray );
        if ( !dumpOPCITEMRESULT ( result ) )
        {
            return;
        }

        // get the server handle array
        final Integer[] serverHandles = new Integer[itemArray.length];
        for ( int i = 0; i < itemArray.length; i++ )
        {
            serverHandles[i] = new Integer ( result.get ( i ).getValue ().getServerHandle () );
        }

        // set them active
        System.out.println ( "Activate" );
        final ResultSet<Integer> resultSet = itemManagement.setActiveState ( true, serverHandles );
        for ( final Result<Integer> resultEntry : resultSet )
        {
            System.out.println ( String.format ( "Item: %08X, Error: %08X", resultEntry.getValue (), resultEntry.getErrorCode () ) );
        }

        // set client handles
        System.out.println ( "Set client handles" );
        final Integer[] clientHandles = new Integer[serverHandles.length];
        for ( int i = 0; i < serverHandles.length; i++ )
        {
            clientHandles[i] = i;
        }
        itemManagement.setClientHandles ( serverHandles, clientHandles );

        System.out.println ( "Create async IO 2.0 object" );
        final OPCAsyncIO2 asyncIO2 = group.getAsyncIO2 ();

        // connect handler
        System.out.println ( "attach" );
        final EventHandler eventHandler = group.attach ( new DumpDataCallback () );

        System.out.println ( "attach..enable" );
        asyncIO2.setEnable ( true );

        System.out.println ( "attach..refresh" );
        final int cancelId = asyncIO2.refresh ( OPCDATASOURCE.OPC_DS_DEVICE, 1 );
        System.out.println ( "Cancel ID: " + cancelId );

        System.out.println ( "attach..read" );
        final AsyncResult asyncResult = asyncIO2.read ( 2, serverHandles );
        System.out.println ( String.format ( "attach..read..cancelId: %08X", asyncResult.getCancelId () ) );

        // sleep
        try
        {
            System.out.println ( "Waiting..." );
            Thread.sleep ( 10 * 1000 );
        }
        catch ( final InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }

        System.out.println ( "Detaching" );
        eventHandler.detach ();

        // set them inactive
        System.out.println ( "In-Active" );
        itemManagement.setActiveState ( false, serverHandles );

        // finally remove them again
        System.out.println ( "Remove" );
        itemManagement.remove ( serverHandles );
    }

    public static void main ( final String[] args ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        final TestConfiguration configuration = new MatrikonSimulationServerConfiguration ();

        OPCServer server = null;
        try
        {
            JISystem.setAutoRegisteration ( true );

            _session = JISession.createSession ( args[1], args[2], args[3] );

            //JIComServer comServer = new JIComServer ( JIClsid.valueOf ( configuration.getCLSID () ), args[0], _session );
            final JIComServer comServer = new JIComServer ( JIProgId.valueOf ( configuration.getProgId () ), args[0], _session );

            final IJIComObject serverObject = comServer.createInstance ();
            server = new OPCServer ( serverObject );

            final OPCGroupStateMgt group = server.addGroup ( "test", true, 100, 1234, 60, 0.0f, 1033 );

            testItems ( server, group, configuration.getReadItems () );
            server.removeGroup ( group, true );
        }
        catch ( final JIException e )
        {
            e.printStackTrace ();
            showError ( server, e.getErrorCode () );
        }
        finally
        {
            if ( _session != null )
            {
                JISession.destroySession ( _session );
            }
            _session = null;
        }
    }
}
