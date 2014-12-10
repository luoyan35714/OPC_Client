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

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIFlags;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIVariant;

public class VariantDumper
{

    static protected void dumpArray ( final String prefix, final JIArray array ) throws JIException
    {
        System.out.println ( prefix + String.format ( "IsConformant: %s, IsVarying: %s", array.isConformant () ? "yes" : "no", array.isVarying () ? "yes" : "no" ) );
        System.out.println ( prefix + String.format ( "Dimensions: %d", array.getDimensions () ) );
        for ( int i = 0; i < array.getDimensions (); i++ )
        {
            System.out.println ( prefix + String.format ( "Dimension #%d: Upper Bound: %d", i, array.getUpperBounds ()[i] ) );
        }

        final Object o = array.getArrayInstance ();
        System.out.println ( prefix + "Array Instance: " + o.getClass () );
        final Object[] a = (Object[])o;
        System.out.println ( prefix + "Array Size: " + a.length );

        for ( final Object value : a )
        {
            dumpValue ( prefix + "\t", value );
        }
    }

    static public void dumpValue ( final Object value ) throws JIException
    {
        dumpValue ( "", value );
    }

    static protected void dumpValue ( final String prefix, final Object value ) throws JIException
    {
        if ( value instanceof JIVariant )
        {
            System.out.println ( prefix + "JIVariant" );
            final JIVariant variant = (JIVariant)value;
            System.out.println ( prefix + String.format ( "IsArray: %s, IsByRef: %s, IsNull: %s", variant.isArray () ? "yes" : "no", variant.isByRefFlagSet () ? "yes" : "no", variant.isNull () ? "yes" : "no" ) );

            if ( variant.isArray () )
            {
                dumpArray ( prefix, variant.getObjectAsArray () );
            }
            else
            {
                dumpValue ( prefix + "\t", variant.getObject () );
            }
        }
        else if ( value instanceof JIString )
        {
            final JIString string = (JIString)value;

            String strType;
            switch ( string.getType () )
            {
            case JIFlags.FLAG_REPRESENTATION_STRING_BSTR:
                strType = "BSTR";
                break;
            case JIFlags.FLAG_REPRESENTATION_STRING_LPCTSTR:
                strType = "LPCSTR";
                break;
            case JIFlags.FLAG_REPRESENTATION_STRING_LPWSTR:
                strType = "LPWSTR";
                break;
            default:
                strType = "unknown";
                break;
            }
            System.out.println ( prefix + String.format ( "JIString: '%s' (%s)", string.getString (), strType ) );
        }
        else if ( value instanceof Double )
        {
            System.out.println ( prefix + "Double: " + value );
        }
        else if ( value instanceof Float )
        {
            System.out.println ( prefix + "Float: " + value );
        }
        else if ( value instanceof Byte )
        {
            System.out.println ( prefix + "Byte: " + value );
        }
        else if ( value instanceof Character )
        {
            System.out.println ( prefix + "Character: " + value );
        }
        else if ( value instanceof Integer )
        {
            System.out.println ( prefix + "Integer: " + value );
        }
        else if ( value instanceof Short )
        {
            System.out.println ( prefix + "Short: " + value );
        }
        else if ( value instanceof Long )
        {
            System.out.println ( prefix + "Long: " + value );
        }
        else if ( value instanceof Boolean )
        {
            System.out.println ( prefix + "Boolean: " + value );
        }

        else
        {
            System.out.println ( prefix + String.format ( "Unknown value type (%s): %s", value.getClass (), value.toString () ) );
        }
    }

}
