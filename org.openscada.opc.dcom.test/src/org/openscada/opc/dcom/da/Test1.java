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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JIProgId;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.common.EventHandler;
import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.Result;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.common.impl.OPCCommon;
import org.openscada.opc.dcom.da.impl.OPCBrowseServerAddressSpace;
import org.openscada.opc.dcom.da.impl.OPCGroupStateMgt;
import org.openscada.opc.dcom.da.impl.OPCItemIO;
import org.openscada.opc.dcom.da.impl.OPCItemMgt;
import org.openscada.opc.dcom.da.impl.OPCItemProperties;
import org.openscada.opc.dcom.da.impl.OPCServer;
import org.openscada.opc.dcom.da.impl.OPCSyncIO;

public class Test1
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

    public static void showAccessPaths ( final OPCBrowseServerAddressSpace browser, final String id ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        for ( final String i : browser.browseAccessPaths ( id ).asCollection () )
        {
            System.out.println ( "AccessPath Entry: " + i );
        }
    }

    public static void browseTree ( final OPCBrowseServerAddressSpace browser ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        System.out.println ( "Showing hierarchial address space" );
        System.out.println ( String.format ( "Organization: %s", browser.queryOrganization () ) );

        if ( !browser.queryOrganization ().equals ( OPCNAMESPACETYPE.OPC_NS_HIERARCHIAL ) )
        {
            return;
        }

        browser.changePosition ( null, OPCBROWSEDIRECTION.OPC_BROWSE_TO );
        browseTree ( browser, 0 );
    }

    protected static void browseTree ( final OPCBrowseServerAddressSpace browser, final int level ) throws JIException, IllegalArgumentException, UnknownHostException
    {
        final StringBuilder indent = new StringBuilder ( level );
        for ( int i = 0; i < level; i++ )
        {
            indent.append ( '\t' );
        }
        for ( final String item : browser.browse ( OPCBROWSETYPE.OPC_LEAF, "", 0, JIVariant.VT_EMPTY ).asCollection () )
        {
            System.out.println ( indent + "Leaf: " + item );
            System.out.println ( indent + "\tName: " + browser.getItemID ( item ) );
        }

        for ( final String item : browser.browse ( OPCBROWSETYPE.OPC_BRANCH, "", 0, JIVariant.VT_EMPTY ).asCollection () )
        {
            System.out.println ( indent + "Branch: " + item );
            browser.changePosition ( item, OPCBROWSEDIRECTION.OPC_BROWSE_DOWN );
            browseTree ( browser, level + 1 );
            browser.changePosition ( null, OPCBROWSEDIRECTION.OPC_BROWSE_UP );
        }
    }

    public static void browseFlat ( final OPCBrowseServerAddressSpace browser ) throws JIException, IllegalArgumentException, UnknownHostException
    {
        System.out.println ( String.format ( "Organization: %s", browser.queryOrganization () ) );
        browser.changePosition ( null, OPCBROWSEDIRECTION.OPC_BROWSE_TO );

        System.out.println ( "Showing flat address space" );
        for ( final String id : browser.browse ( OPCBROWSETYPE.OPC_FLAT, "", 0, JIVariant.VT_EMPTY ).asCollection () )
        {
            System.out.println ( "Item: " + id );
            //showAccessPaths ( browser, id );
        }
    }

    public static void dumpGroupState ( final OPCGroupStateMgt group ) throws JIException
    {
        final OPCGroupState state = group.getState ();

        System.out.println ( "Name: " + state.getName () );
        System.out.println ( "Active: " + state.isActive () );
        System.out.println ( "Update Rate: " + state.getUpdateRate () );
        System.out.println ( "Time Bias: " + state.getTimeBias () );
        System.out.println ( "Percent Deadband: " + state.getPercentDeadband () );
        System.out.println ( "Locale ID: " + state.getLocaleID () );
        System.out.println ( "Client Handle: " + state.getClientHandle () );
        System.out.println ( "Server Handle: " + state.getServerHandle () );
    }

    public static void dumpItemProperties2 ( final OPCItemProperties itemProperties, final String itemID, final int... ids ) throws JIException
    {
        final KeyedResultSet<Integer, JIVariant> values = itemProperties.getItemProperties ( itemID, ids );
        for ( final KeyedResult<Integer, JIVariant> entry : values )
        {
            System.out.println ( String.format ( "ID: %d, Value: %s, Error Code: %08x", entry.getKey (), entry.getValue ().toString (), entry.getErrorCode () ) );
        }
    }

    public static void dumpItemPropertiesLookup ( final OPCItemProperties itemProperties, final String itemID, final int... ids ) throws JIException
    {
        final KeyedResultSet<Integer, String> values = itemProperties.lookupItemIDs ( itemID, ids );
        for ( final KeyedResult<Integer, String> entry : values )
        {
            System.out.println ( String.format ( "ID: %d, Item ID: %s, Error Code: %08x", entry.getKey (), entry.getValue (), entry.getErrorCode () ) );
        }
    }

    public static void dumpItemProperties ( final OPCItemProperties itemProperties, final String itemID ) throws JIException
    {
        final Collection<PropertyDescription> properties = itemProperties.queryAvailableProperties ( itemID );
        final int[] ids = new int[properties.size ()];

        System.out.println ( String.format ( "Item Properties for '%s' (count:%d)", itemID, properties.size () ) );
        int i = 0;
        for ( final PropertyDescription pd : properties )
        {
            ids[i] = pd.getId ();
            System.out.println ( "ID: " + pd.getId () );
            System.out.println ( "Description: " + pd.getDescription () );
            System.out.println ( "Variable Type: " + pd.getVarType () );
            i++;
        }

        System.out.println ( "Lookup" );
        dumpItemPropertiesLookup ( itemProperties, itemID, ids );

        System.out.println ( "Query" );
        dumpItemProperties2 ( itemProperties, itemID, ids );
    }

    public static void queryItems ( final OPCItemIO itemIO, final String... items ) throws JIException
    {
        final List<IORequest> requests = new LinkedList<IORequest> ();
        for ( final String item : items )
        {
            requests.add ( new IORequest ( item, 0 ) );
        }
        itemIO.read ( requests.toArray ( new IORequest[0] ) );
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

    public static void writeItems ( final OPCServer server, final OPCGroupStateMgt group, final WriteTest... writeTests ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        System.out.println ( "Write items" );

        final OPCItemMgt itemManagement = group.getItemManagement ();
        final OPCITEMDEF[] defs = new OPCITEMDEF[writeTests.length];
        for ( int i = 0; i < writeTests.length; i++ )
        {
            final OPCITEMDEF def = new OPCITEMDEF ();
            def.setActive ( true );
            def.setItemID ( writeTests[i].getItemID () );
            //def.setRequestedDataType ( (short)writeTests[i].getValue ().getType () );
            defs[i] = def;

            System.out.println ( String.format ( "%s <= (%d) %s", writeTests[i].getItemID (), writeTests[i].getValue ().getType (), writeTests[i].getValue ().toString () ) );
        }

        System.out.println ( "Add to group" );
        final KeyedResultSet<OPCITEMDEF, OPCITEMRESULT> result = itemManagement.add ( defs );
        final WriteRequest[] writeRequests = new WriteRequest[writeTests.length];
        final Integer[] serverHandles = new Integer[writeTests.length];
        for ( int i = 0; i < writeTests.length; i++ )
        {
            if ( result.get ( i ).getErrorCode () != 0 )
            {
                throw new JIException ( result.get ( i ).getErrorCode () );
            }

            writeRequests[i] = new WriteRequest ( result.get ( i ).getValue ().getServerHandle (), writeTests[i].getValue () );
            serverHandles[i] = writeRequests[i].getServerHandle ();

            System.out.println ( String.format ( "Item: %s, VT: %d", writeTests[i].getItemID (), result.get ( i ).getValue ().getCanonicalDataType () ) );
        }

        System.out.println ( "Perform write" );
        final OPCSyncIO syncIO = group.getSyncIO ();
        final ResultSet<WriteRequest> writeResults = syncIO.write ( writeRequests );
        for ( int i = 0; i < writeTests.length; i++ )
        {
            final Result<WriteRequest> writeResult = writeResults.get ( i );
            System.out.println ( String.format ( "ItemID: %s, ErrorCode: %08X", writeTests[i].getItemID (), writeResult.getErrorCode () ) );
            if ( writeResult.getErrorCode () != 0 )
            {
                showError ( server, writeResult.getErrorCode () );
            }
        }

        // finally remove them again
        System.out.println ( "Remove from group" );
        itemManagement.remove ( serverHandles );

        System.out.println ( "Write items...complete" );
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
        // OPCAsyncIO2 asyncIO2 = group.getAsyncIO2 ();
        // connect handler

        System.out.println ( "attach" );
        final EventHandler eventHandler = group.attach ( new DumpDataCallback () );
        /*
         System.out.println ( "attach..enable" );
         asyncIO2.setEnable ( true );
         System.out.println ( "attach..refresh" );
         asyncIO2.refresh ( (short)1, 1 );
         */
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

        eventHandler.detach ();

        // sync IO - read
        final OPCSyncIO syncIO = group.getSyncIO ();
        final KeyedResultSet<Integer, OPCITEMSTATE> itemState = syncIO.read ( OPCDATASOURCE.OPC_DS_DEVICE, serverHandles );
        for ( final KeyedResult<Integer, OPCITEMSTATE> itemStateEntry : itemState )
        {
            final int errorCode = itemStateEntry.getErrorCode ();
            System.out.println ( String.format ( "Server ID: %08X, Value: %s, Timestamp: %d/%d (%Tc), Quality: %d, Error: %08X", itemStateEntry.getKey (), itemStateEntry.getValue ().getValue (), itemStateEntry.getValue ().getTimestamp ().getHigh (), itemStateEntry.getValue ().getTimestamp ().getLow (), itemStateEntry.getValue ().getTimestamp ().asCalendar (), itemStateEntry.getValue ().getQuality (), errorCode ) );
            if ( errorCode != 0 )
            {
                showError ( server, errorCode );
            }
        }

        // set them inactive
        System.out.println ( "In-Active" );
        itemManagement.setActiveState ( false, serverHandles );

        // finally remove them again
        System.out.println ( "Remove" );
        itemManagement.remove ( serverHandles );
    }

    public static void dumpServerStatus ( final OPCServer server ) throws JIException
    {
        final OPCSERVERSTATUS status = server.getStatus ();

        System.out.println ( "===== SERVER STATUS ======" );
        System.out.println ( "State: " + status.getServerState ().toString () );
        System.out.println ( "Vendor: " + status.getVendorInfo () );
        System.out.println ( String.format ( "Version: %d.%d.%d", status.getMajorVersion (), status.getMinorVersion (), status.getBuildNumber () ) );
        System.out.println ( "Groups: " + status.getGroupCount () );
        System.out.println ( "Bandwidth: " + status.getBandWidth () );
        System.out.println ( String.format ( "Start Time: %tc", status.getStartTime ().asCalendar () ) );
        System.out.println ( String.format ( "Current Time: %tc", status.getCurrentTime ().asCalendar () ) );
        System.out.println ( String.format ( "Last Update Time: %tc", status.getLastUpdateTime ().asCalendar () ) );
        System.out.println ( "===== SERVER STATUS ======" );
    }

    public static void enumerateGroups ( final OPCServer server, final OPCENUMSCOPE scope ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        System.out.println ( "Enum Groups: " + scope.toString () );

        for ( final String group : server.getGroups ( scope ).asCollection () )
        {
            System.out.println ( "Group: " + group );
        }
    }

    @SuppressWarnings ( "unused" )
    public static void main ( final String[] args ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        final TestConfiguration configuration = new MatrikonSimulationServerConfiguration ();

        OPCServer server = null;
        try
        {
            JISystem.setAutoRegisteration ( true );

            _session = JISession.createSession ( args[1], args[2], args[3] );
            // OPCServer server = new OPCServer ( "127.0.0.1", JIProgId.valueOf
            // ( session, "Matrikon.OPC.Simulation.1" ),
            // session );

            //JIComServer comServer = new JIComServer ( JIClsid.valueOf ( configuration.getCLSID () ), args[0], _session );
            final JIComServer comServer = new JIComServer ( JIProgId.valueOf ( configuration.getProgId () ), args[0], _session );

            final IJIComObject serverObject = comServer.createInstance ();
            server = new OPCServer ( serverObject );
            dumpServerStatus ( server );

            /*
             OPCCommon common = server.getCommon ();
             common.setLocaleID ( 1033 );
             System.out.println ( String.format ( "LCID: %d", common.getLocaleID () ) );
             common.setClientName ( "test" );
             for ( Integer i : common.queryAvailableLocaleIDs () )
             {
             System.out.println ( String.format ( "Available LCID: %d", i ) );
             }
             */

            final OPCBrowseServerAddressSpace serverBrowser = server.getBrowser ();
            browseFlat ( serverBrowser );
            /*
             browseTree ( serverBrowser );
             */

            final OPCGroupStateMgt group = server.addGroup ( "test", true, 100, 1234, 60, 0.0f, 1033 );
            /*
             group.setName ( "test2" );
             OPCGroupStateMgt group2 = group.clone ( "test" );
             group = server.getGroupByName ( "test2" );
             group.setState ( null, false, null, null, null, null );
             group.setState ( null, true, null, null, null, null );
             dumpGroupState ( group );
             dumpGroupState ( group2 );
             */
            testItems ( server, group, configuration.getReadItems () );
            if ( configuration.getWriteItems () != null )
            {
                writeItems ( server, group, configuration.getWriteItems () );
            }

            final OPCItemProperties itemProperties = server.getItemPropertiesService ();
            //dumpItemProperties ( itemProperties, "Saw-toothed Waves.Int" );

            final OPCItemIO itemIO = server.getItemIOService ();
            //queryItems ( itemIO, "Saw-toothed Waves.Int" );

            enumerateGroups ( server, OPCENUMSCOPE.OPC_ENUM_PUBLIC );
            enumerateGroups ( server, OPCENUMSCOPE.OPC_ENUM_PRIVATE );
            enumerateGroups ( server, OPCENUMSCOPE.OPC_ENUM_ALL );

            // clean up
            server.removeGroup ( group, true );
            //server.removeGroup ( group2, true );

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
