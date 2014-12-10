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

import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIFlags;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;

/**
 * A sample that reads an item and writes back the result. You will need a
 * read/write item for this to work.
 * @author Jens Reimann &lt;jens.reimann@th4-systems.com&gt;
 *
 */
public class OPCTest7
{
    @SuppressWarnings ( "unused" )
    public static void main ( final String[] args ) throws Throwable
    {
        // create connection information
        final ConnectionInformation ci = new ConnectionInformation ();
        ci.setHost ( args[0] );
        ci.setDomain ( args[1] );
        ci.setUser ( args[2] );
        ci.setPassword ( args[3] );
        ci.setClsid ( args[4] );

        final String itemName = args[5];

        // create a new server
        final Server server = new Server ( ci, Executors.newSingleThreadScheduledExecutor () );
        try
        {
            // connect to server
            server.connect ();

            // Add a new group
            final Group group = server.addGroup ( "test" );

            // Add a new item to the group
            final Item item = group.addItem ( itemName );

            final JIString[] sdata = new JIString[] { new JIString ( "ab", JIFlags.FLAG_REPRESENTATION_STRING_BSTR ), new JIString ( "cd", JIFlags.FLAG_REPRESENTATION_STRING_BSTR ), new JIString ( "ef", JIFlags.FLAG_REPRESENTATION_STRING_BSTR ) };
            final Double[] ddata = new Double[] { 1.1, 2.2, 3.3 };
            final Boolean[] bdata = new Boolean[] { true, false, true, false };
            final Integer[] idata = new Integer[] { 1202, 1203, 1204 };
            final Long[] ldata = new Long[] { 12020001L, 12030001L, 12040001L };
            final Float[] fdata = new Float[] { 1.1f, 1.2f, 1.3f };
            final Byte[] bydata = new Byte[] { 1, 2, 3 };
            final Character[] cdata = new Character[] { 'A', 'B', 'C' };

            final JIArray array = new JIArray ( ddata, true );
            final JIVariant value = new JIVariant ( array );

            System.out.println ( "============= PHASE 1 ============= " );

            // dump the value
            VariantDumper.dumpValue ( value );

            System.out.println ( "============= PHASE 2 ============= " );

            // now write it to the item
            item.write ( value );
            Thread.sleep ( 2500 );

            System.out.println ( "============= PHASE 3 ============= " );

            // now read the value back and dump it
            final ItemState itemState = item.read ( true );
            VariantDumper.dumpValue ( itemState.getValue () );

            System.out.println ( "============= PHASE 4 ============= " );

            // and write the value just read in
            item.write ( itemState.getValue () );

            System.out.println ( "============= COMPLETE ============= " );
        }
        catch ( final JIException e )
        {
            System.out.println ( String.format ( "%08X: %s", e.getErrorCode (), server.getErrorMessage ( e.getErrorCode () ) ) );
        }
    }
}
