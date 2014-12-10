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

import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.ResultSet;

public class DumpDataCallback implements IOPCDataCallback
{

    public void cancelComplete ( final int transactionId, final int serverGroupHandle )
    {
        System.out.println ( String.format ( "cancelComplete: %08X, Group: %08X", transactionId, serverGroupHandle ) );
    }

    public void dataChange ( final int transactionId, final int serverGroupHandle, final int masterQuality, final int masterErrorCode, final KeyedResultSet<Integer, ValueData> result )
    {
        System.out.println ( String.format ( "dataChange: %d, Group: %08X, MasterQ: %d, Error: %d", transactionId, serverGroupHandle, masterQuality, masterErrorCode ) );

        for ( final KeyedResult<Integer, ValueData> entry : result )
        {
            System.out.println ( String.format ( "%08X - Error: %08X, Quality: %d, %Tc - %s", entry.getKey (), entry.getErrorCode (), entry.getValue ().getQuality (), entry.getValue ().getTimestamp (), entry.getValue ().getValue ().toString () ) );
        }
    }

    public void readComplete ( final int transactionId, final int serverGroupHandle, final int masterQuality, final int masterErrorCode, final KeyedResultSet<Integer, ValueData> result )
    {
        System.out.println ( String.format ( "readComplete: %d, Group: %08X, MasterQ: %d, Error: %d", transactionId, serverGroupHandle, masterQuality, masterErrorCode ) );

        for ( final KeyedResult<Integer, ValueData> entry : result )
        {
            System.out.println ( String.format ( "%08X - Error: %08X, Quality: %d, %Tc - %s", entry.getKey (), entry.getErrorCode (), entry.getValue ().getQuality (), entry.getValue ().getTimestamp (), entry.getValue ().getValue ().toString () ) );
        }
    }

    public void writeComplete ( final int transactionId, final int serverGroupHandle, final int masterErrorCode, final ResultSet<Integer> result )
    {
        // TODO Auto-generated method stub
    }

}
