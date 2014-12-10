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

import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.browser.BaseBrowser;
import org.openscada.opc.lib.da.browser.Branch;
import org.openscada.opc.lib.da.browser.Leaf;
import org.openscada.opc.lib.da.browser.TreeBrowser;

/**
 * Another test showing the browser interfaces
 * @author Jens Reimann <jens.reimann@th4-systems.com>
 *
 */
public class OPCTest3
{

    private static void dumpLeaf ( final String prefix, final Leaf leaf )
    {
        System.out.println ( prefix + "Leaf: " + leaf.getName () + " [" + leaf.getItemId () + "]" );
    }

    private static void dumpBranch ( final String prefix, final Branch branch )
    {
        System.out.println ( prefix + "Branch: " + branch.getName () );
    }

    public static void dumpTree ( final Branch branch, final int level )
    {
        final StringBuilder sb = new StringBuilder ();
        for ( int i = 0; i < level; i++ )
        {
            sb.append ( "  " );
        }
        final String indent = sb.toString ();

        for ( final Leaf leaf : branch.getLeaves () )
        {
            dumpLeaf ( indent, leaf );
        }
        for ( final Branch subBranch : branch.getBranches () )
        {
            dumpBranch ( indent, subBranch );
            dumpTree ( subBranch, level + 1 );
        }
    }

    public static void main ( final String[] args ) throws Throwable
    {
        // create connection information
        final ConnectionInformation ci = new ConnectionInformation ();
        ci.setHost ( args[0] );
        ci.setDomain ( args[1] );
        ci.setUser ( args[2] );
        ci.setPassword ( args[3] );
        ci.setClsid ( args[4] );

        // create a new server
        final Server server = new Server ( ci, Executors.newSingleThreadScheduledExecutor () );
        try
        {
            // connect to server
            server.connect ();

            // browse flat
            final BaseBrowser flatBrowser = server.getFlatBrowser ();
            if ( flatBrowser != null )
            {
                for ( final String item : server.getFlatBrowser ().browse ( "" ) )
                {
                    System.out.println ( item );
                }
            }

            // browse tree
            final TreeBrowser treeBrowser = server.getTreeBrowser ();
            if ( treeBrowser != null )
            {
                dumpTree ( treeBrowser.browse (), 0 );
            }

            // browse tree manually
            browseTree ( "", treeBrowser, new Branch () );
        }
        catch ( final JIException e )
        {
            e.printStackTrace ();
            System.out.println ( String.format ( "%08X: %s", e.getErrorCode (), server.getErrorMessage ( e.getErrorCode () ) ) );
        }
    }

    private static void browseTree ( final String prefix, final TreeBrowser treeBrowser, final Branch branch ) throws IllegalArgumentException, UnknownHostException, JIException
    {
        treeBrowser.fillLeaves ( branch );
        treeBrowser.fillBranches ( branch );

        for ( final Leaf leaf : branch.getLeaves () )
        {
            dumpLeaf ( "M - " + prefix + " ", leaf );
        }
        for ( final Branch subBranch : branch.getBranches () )
        {
            dumpBranch ( "M - " + prefix + " ", subBranch );
            browseTree ( prefix + " ", treeBrowser, subBranch );
        }
    }
}
