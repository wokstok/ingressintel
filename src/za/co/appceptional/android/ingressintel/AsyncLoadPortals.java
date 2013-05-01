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

import com.google.api.services.fusiontables.Fusiontables;
import com.google.api.services.fusiontables.Fusiontables.Query.SqlGet;
import com.google.api.services.fusiontables.model.Sqlresponse;
import com.google.api.services.fusiontables.model.TableList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import za.co.appceptional.android.ingressintel.PortalsFragment;

/**
 * Asynchronously load the tasks.
 * 
 * @author Yaniv Inbar
 */
class AsyncLoadPortals extends PortalAsyncTask
{

	AsyncLoadPortals(PortalsFragment fragment)
	{
		super(fragment);
	}

	@Override
	protected void doInBackground() throws IOException {
		List<PortalInfo> result = new ArrayList<PortalInfo>();

		String tableName = "1C_OSNm_ecdmDqMbfDzZ62fkJaYwgk9pIAYBeLEY";
		//String sqlstring = "select * from " + tableName + ";";
		String sqlstring = "select ROWID, Name, Location, DateCreated, DateModified, Team, Level from " + tableName + ";";
		SqlGet sqlGet = client.query().sqlGet(sqlstring);
		Sqlresponse r = sqlGet.execute();
		for (List<Object> l : r.getRows())
		{
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

	static void run(PortalsFragment fragment) {
		new AsyncLoadPortals(fragment).execute();
	}
}
