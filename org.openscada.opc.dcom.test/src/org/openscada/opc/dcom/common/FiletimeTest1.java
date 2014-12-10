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

package org.openscada.opc.dcom.common;

import junit.framework.Assert;

import org.junit.Test;

public class FiletimeTest1
{
    public static void main ( final String[] args )
    {
        FILETIME ft = new FILETIME ( 29949427, 2139800608 );
        System.out.println ( String.format ( "%s, %tc", ft, ft.asCalendar () ) );

        ft = new FILETIME ( 29949427, -2145016688 );
        System.out.println ( String.format ( "%s, %tc", ft, ft.asCalendar () ) );
    }

    @Test
    public void test ()
    {
        assertEquals ( "Thu Aug 14 11:52:43 CEST 2008", new FILETIME ( 29949427, 2139800608 ) );
        assertEquals ( "Thu Aug 14 11:52:44 CEST 2008", new FILETIME ( 29949427, -2145016688 ) );
    }

    protected void assertEquals ( final String expected, final FILETIME actual )
    {
        Assert.assertEquals ( expected, String.format ( "%tc", actual.asCalendar () ) );
    }

    @Test
    public void test2 ()
    {
        FILETIME last = null;
        for ( int i = 0; i < 10000; i++ )
        {
            final FILETIME ft = new FILETIME ( 29949427 + i, 2139800608 + i );
            Assert.assertEquals ( ft.asBigDecimalCalendar (), ft.asCalendar () );

            if ( last != null )
            {
                Assert.assertTrue ( last.asCalendar ().before ( ft.asCalendar () ) );
            }

            last = ft;
        }
    }

    @Test
    public void test3a ()
    {
        for ( int i = 0; i < 10000; i++ )
        {
            final FILETIME ft = new FILETIME ( 29949427 + i, 2139800608 + i );
            ft.asCalendar ();
        }
    }

    @Test
    public void test3b ()
    {
        for ( int i = 0; i < 10000; i++ )
        {
            final FILETIME ft = new FILETIME ( 29949427 + i, 2139800608 + i );
            ft.asBigDecimalCalendar ();
        }
    }
}
