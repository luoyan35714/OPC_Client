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

public class MatrikonSimulationServerConfiguration implements TestConfiguration
{

    public String getCLSID ()
    {
        return "F8582CF2-88FB-11D0-B850-00C0F0104305";
    }

    public String getProgId ()
    {
        return "Matrikon.OPC.Simulation.1";
    }

    public String[] getReadItems ()
    {
        return new String[] { "Saw-toothed Waves.Int2", "Saw-toothed Waves.Int4" };
    }

    public WriteTest[] getWriteItems ()
    {
        return new WriteTest[] { new WriteTest ( "Write Only.Int2", new JIVariant ( (short)1202, false ) ), new WriteTest ( "Write Only.Int4", new JIVariant ( 1202, false ) ) };
    }

}
