/**
* Android ownCloud News
*
* @author David Luhmer
* @copyright 2013 David Luhmer david-dev@live.de
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU AFFERO GENERAL PUBLIC LICENSE
* License as published by the Free Software Foundation; either
* version 3 of the License, or any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU AFFERO GENERAL PUBLIC LICENSE for more details.
*
* You should have received a copy of the GNU Affero General Public
* License along with this library.  If not, see <http://www.gnu.org/licenses/>.
*
*/

package de.luhmer.owncloudnewsreader.reader.owncloud;

import java.util.ArrayList;

import org.json.JSONObject;

import de.luhmer.owncloudnewsreader.data.ConcreteFeedItem;
import de.luhmer.owncloudnewsreader.database.DatabaseConnection;
import de.luhmer.owncloudnewsreader.reader.InsertIntoDatabase;

public class InsertFeedIntoDatabase implements IHandleJsonObject{
	
	DatabaseConnection dbConn;
	ArrayList<ConcreteFeedItem> feeds = new ArrayList<ConcreteFeedItem>();
	
	public InsertFeedIntoDatabase(DatabaseConnection dbConn) {
		this.dbConn = dbConn;
	}
	
    private static ConcreteFeedItem parseFeed(JSONObject e)
	{
    	String faviconLink = e.optString("faviconLink");
        if(faviconLink != null)
            if(faviconLink.equals("null") || faviconLink.trim().equals(""))
                faviconLink = null;
        
        return new ConcreteFeedItem(e.optString("title"), e.optString("folderId"), e.optString("id"), faviconLink, -1);
	}

	@Override
	public boolean performAction(JSONObject jObj) {
		ConcreteFeedItem rssFeed = parseFeed(jObj);
		feeds.add(rssFeed);
        return true;
	}
	
	public void WriteAllToDatabaseNow() {
		InsertIntoDatabase.InsertSubscriptionsIntoDatabase(feeds, dbConn);
	}
}
