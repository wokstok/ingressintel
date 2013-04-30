/*
 * Copyright (c) 2012 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package za.co.appceptional.android.ingressintel;

import com.google.api.client.util.DateTime;
import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.Sql;
import com.google.api.services.fusiontables.Fusiontables.Query.SqlGet;
import com.google.api.services.fusiontables.model.Sqlresponse;
import com.google.api.services.fusiontables.model.TableList;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import za.co.appceptional.android.ingressintel.MainActivity.PortalsFragment;

/**
 * Asynchronously load the tasks.
 * 
 * @author Yaniv Inbar
 */
class AsyncAddPortal extends PortalAsyncTask
{
	PortalInfo portalInfo;
	
	AsyncAddPortal(PortalsFragment fragment, PortalInfo portalInfo)
	{
		super(fragment);
		this.portalInfo = portalInfo;
	}

	@Override
	protected void doInBackground() throws IOException {
		List<PortalInfo> result = new ArrayList<PortalInfo>();

		String tableName = "1C_OSNm_ecdmDqMbfDzZ62fkJaYwgk9pIAYBeLEY";
		
		String sqlstring =
				"insert into " + tableName + 
				" (ID, Name, Location, DateModified, DateCreated, Team, Level) values " +
				"('" + portalInfo.id + "'," +
				"'" + portalInfo.name + "'," +
				"'" + portalInfo.location + "'," +
				"'" + portalInfo.dateCreated + "'," + 
				"'" + portalInfo.dateModified + "'," +
				"'" + portalInfo.team + "'," + 
				+ portalInfo.level + ")";
		Sql sql = client.query().sql(sqlstring);
		Sqlresponse r = sql.execute ();
		
		sqlstring = "select * from " + tableName + ";";
		SqlGet sqlGet = client.query().sqlGet(sqlstring);
		r = sqlGet.execute();
		for (List<Object> l : r.getRows()) {
			PortalInfo portalInfo = new PortalInfo ();
			portalInfo.id = l.get(0).toString ();
			portalInfo.name = l.get(1).toString ();
			portalInfo.location = l.get(2).toString();
			portalInfo.dateCreated = l.get(3).toString();
			portalInfo.dateModified = l.get(4).toString();
			portalInfo.team = l.get(5).toString();
			portalInfo.level = Integer.parseInt(l.get(6).toString());
			
			result.add (portalInfo);
		}

		super.tasksList = result;
	}

	static void run(PortalsFragment fragment, PortalInfo portalInfo) {
		new AsyncAddPortal(fragment, portalInfo).execute();
	}
}
