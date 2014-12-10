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

import org.jinterop.dcom.core.JIVariant;

public class WriteTest
{
    private String _itemID = "";

    private JIVariant _value = JIVariant.EMPTY ();

    public WriteTest ( final String itemID, final JIVariant value )
    {
        super ();
        this._itemID = itemID;
        this._value = value;
    }

    public String getItemID ()
    {
        return this._itemID;
    }

    public void setItemID ( final String itemID )
    {
        this._itemID = itemID;
    }

    public JIVariant getValue ()
    {
        return this._value;
    }

    public void setValue ( final JIVariant value )
    {
        this._value = value;
    }
}
