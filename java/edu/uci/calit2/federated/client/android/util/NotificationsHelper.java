/*
 *  This file is part of AntMonitor <https://athinagroup.eng.uci.edu/projects/antmonitor/>.
 *  Copyright (C) 2018 Anastasia Shuba and the UCI Networking Group
 *  <https://athinagroup.eng.uci.edu>, University of California, Irvine.
 *
 *  AntMonitor is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, version 2 of the License.
 *
 *  AntMonitor is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with AntMonitor. If not, see <http://www.gnu.org/licenses/>.
 */
package edu.uci.calit2.federated.client.android.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class containing the various methods for managing notifications
 *
 * @author Hieu Le (hieul@uci.edu)
 */
public class NotificationsHelper {

    private static final String TAG = NotificationsHelper.class.getSimpleName();

    private static final Map leakNotifications = new HashMap<>();
    
    public static void trackLeakNotification(int id, String app, String piiLabel) {
        leakNotifications.put(app+piiLabel, id);
    }

    public static boolean hasLeakNotification(String app, String piiLabel) {
        return leakNotifications.containsKey(app+piiLabel);
    }

    public static void removeLeakNotification(int id, String app, String piiLabel) {
        String compositeKey = app+piiLabel;
        if (leakNotifications.containsKey(compositeKey) && (int)leakNotifications.get(app+piiLabel) == id) {
            leakNotifications.remove(compositeKey);
        }
    }




}
