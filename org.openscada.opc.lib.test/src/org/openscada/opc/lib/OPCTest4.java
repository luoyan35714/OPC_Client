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

package org.openscada.opc.lib;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Async20Access;
import org.openscada.opc.lib.da.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Another test showing the "Access" interface with the Async20Access implementation.
 * @author Jens Reimann <jens.reimann@th4-systems.com>
 *
 */
public class OPCTest4
{
    private static Logger _log = LoggerFactory.getLogger ( OPCTest4.class );

    public static void main ( final String[] args ) throws Throwable
    {
        // create connection information
        final ConnectionInformation ci = new ConnectionInformation ();
        ci.setHost ( args[0] );
        ci.setDomain ( args[1] );
        ci.setUser ( args[2] );
        ci.setPassword ( args[3] );
        ci.setClsid ( args[4] );

        final Set<String> items = new HashSet<String> ();
        for ( int i = 5; i < args.length; i++ )
        {
            items.add ( args[i] );
        }
        if ( items.isEmpty () )
        {
            items.add ( "Saw-toothed Waves.Int2" );
        }

        // create a new server
        final Server server = new Server ( ci, Executors.newSingleThreadScheduledExecutor () );
        try
        {
            // connect to server
            server.connect ();

            // add sync access
            final AccessBase access = new Async20Access ( server, 100, false );
            for ( final String itemId : items )
            {
                access.addItem ( itemId, new DataCallbackDumper () );
            }

            // start reading
            access.bind ();

            // wait a little bit
            _log.info ( "Sleep for some seconds to give events a chance..." );
            Thread.sleep ( 10 * 1000 );
            _log.info ( "Returned from sleep" );

            // stop reading
            access.unbind ();
        }
        catch ( final JIException e )
        {
            System.out.println ( String.format ( "%08X: %s", e.getErrorCode (), server.getErrorMessage ( e.getErrorCode () ) ) );
        }
    }
}
