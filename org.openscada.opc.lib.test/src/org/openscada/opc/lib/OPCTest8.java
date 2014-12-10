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

import java.util.Collection;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

/**
 * A sample that queries the server browser interface
 * @author Jens Reimann &lt;jens.reimann@th4-systems.com&gt;
 *
 */
public class OPCTest8
{
    protected static void showDetails ( final ServerList serverList, final String clsid ) throws JIException
    {
        final ClassDetails cd = serverList.getDetails ( clsid );
        if ( cd != null )
        {
            System.out.println ( cd.getProgId () + " = " + cd.getDescription () );
        }
        else
        {
            System.out.println ( "unknown" );
        }
    }

    public static void main ( final String[] args ) throws Throwable
    {
        final ServerList serverList = new ServerList ( args[0], args[2], args[3], args[1] );

        final String cls = serverList.getClsIdFromProgId ( "Matrikon.OPC.Simulation.1" );
        System.out.println ( "Matrikon OPC Simulation Server: " + cls );
        showDetails ( serverList, cls );

        final Collection<ClassDetails> detailsList = serverList.listServersWithDetails ( new Category[] { Categories.OPCDAServer20 }, new Category[] {} );

        for ( final ClassDetails details : detailsList )
        {
            System.out.println ( String.format ( "Found: %s", details.getClsId () ) );
            System.out.println ( String.format ( "\tProgID: %s", details.getProgId () ) );
            System.out.println ( String.format ( "\tDescription: %s", details.getDescription () ) );
        }
    }
}
