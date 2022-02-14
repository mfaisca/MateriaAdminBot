package com.materiabot.IO.SQL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import com.materiabot.GameElements.Event.EventLink;
import com.materiabot.Utils.Constants;
import com.materiabot.Utils.MessageUtils;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import Shared.BotException;

public class SQLAccess {
	private static MysqlConnectionPoolDataSource dataSource;
	public static final String BOT_TOKEN_KEY = !Constants.DEBUG ? "BOT_TOKEN_KEY" : "BOT_TOKEN_KEY_DEBUG";
	public static final String PATREON_ACCESS_TOKEN = "PATREON_ACCESS_TOKEN";
	public static final String PATREON_TT_ACCESS_TOKEN = "PATREON_TT_ACCESS_TOKEN";
	public static final String CLEVERBOT_TOKEN_KEY = "CLEVERBOT_TOKEN_KEY";
	public static final String REDDIT_PASSWORD = "REDDIT_PASSWORD"; 

	static{ 
		try {
			File f = new File("./resources/Configs.txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
			dataSource = new MysqlConnectionPoolDataSource();
			dataSource.setServerName(br.readLine());
			dataSource.setDatabaseName(br.readLine());
			dataSource.setUser(br.readLine());
			dataSource.setPassword(br.readLine());
			br.close();
		} catch (IOException e) {
			System.out.println("Error loading Database data");
			System.exit(0);
		}
	}
	
	private SQLAccess() {}

	public static Connection getConnection() throws SQLException{ 
		return dataSource.getConnection();
	}
	
	public static String getUnitNameFromNickname(String nick){
		try(ResultSet rs = executeSelect("SELECT unit FROM Unit_Nicknames WHERE nickname = ? OR unit = ?", nick, nick)) {
			if(rs.next())
				return rs.getString("unit");
		} catch (BotException | SQLException e) {
			;
		}
		return null;
	}

	private static PreparedStatement prepare(String query, Object...params) throws SQLException {
		PreparedStatement statement = SQLAccess.getConnection().prepareStatement(query);
		for(int i = 0; i < params.length; i++)
			if(params[i] == null)
				statement.setNull(i+1, Types.NULL);
			else if(params[i].getClass().equals(String.class))
				statement.setString(i+1, (String)params[i]);
			else if(params[i].getClass().equals(Long.class))
				statement.setLong(i+1, (Long)params[i]);
			else if(params[i].getClass().equals(Integer.class))
				statement.setInt(i+1, (Integer)params[i]);
			else if(params[i].getClass().equals(Boolean.class))
				statement.setBoolean(i+1, (Boolean)params[i]);
			else
				throw new RuntimeException("Inserting unknown type into DB: " + params[i].getClass());
		return statement;
	}

	public static synchronized int executeInsert(String query, Object... params) throws BotException{
		try(PreparedStatement ps = prepare(query, params)) {
			int result = ps.executeUpdate();
			ps.getConnection().close();
			return result;
		} catch(Exception e) { 
			final String textParams = Arrays.stream(params).map(o -> o.toString()).reduce((o1, o2) -> o1 + " || " + o2).orElse("null");
			throw new BotException("Error when running the select query '" + query + "' with parameters: " + textParams + ".\nError: " + e.getMessage(), e);
		}
	}

	public static ResultSet executeSelect(String query, Object... params) throws BotException{
		try(PreparedStatement ps = prepare(query, params)) {
			try(ResultSet rs = ps.executeQuery()){
				CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
				crs.populate(rs);
				ps.getConnection().close();
				return crs;
			}
		} catch(Exception e) { 
			final String textParams = Arrays.stream(params).map(o -> o.toString()).reduce((o1, o2) -> o1 + " || " + o2).orElse("null");
			throw new BotException("Error when running the select query '" + query + "' with parameters: " + textParams, e);
		}
	}

	public static String getKeyValue(String key){
		try(ResultSet result = SQLAccess.executeSelect("SELECT * FROM Configs WHERE keyy = ?", key)) {
			String res = null;
			if(result.next())
				res = result.getString("value");
			return res;
		} catch (SQLException | BotException e) {
			System.out.println("Error communicating with the database!");
			if(Constants.getClient() != null)
				MessageUtils.sendWhisper(Constants.QUETZ_ID, e.getLocalizedMessage());
			return null;
		}
	}
	public static void setKeyValue(String key, String value) throws BotException{
		SQLAccess.executeInsert("REPLACE INTO Configs VALUES(?, ?)", key, value);
	}

	public static class Friend{
		public long userId;
		public String region;
		public String fcCode;
		public String unit;
		public String note;
		public String ffootipId;
		public int totalGems = 0;
		public int totalTickets = 0;
		public int skipCount = 0;
		public int ticketCount = 0;
		public int gemCount = 0;
		public int ldPityCount = 0;
		public int btPityCount = 0;

		public static Friend getFriend(Long userId, String region) throws BotException {
			try {
				ResultSet ret = SQLAccess.executeSelect("SELECT * FROM Friend_Codes WHERE userId = ? AND region = ?", userId, region);
				Friend f = new Friend();
				if(!ret.next())
					return null;
				f.userId = ret.getLong("userId");
				f.region = ret.getString("region");
				f.fcCode = ret.getString("code");
				f.unit = ret.getString("unit");
				f.note = ret.getString("note");
				f.ffootipId = ret.getString("ffootipId");
				ret = SQLAccess.executeSelect("SELECT SUM(gemCount) gemTotal, SUM(ticketCount) ticketTotal FROM User_Banner_Costs WHERE userId = ?", userId);
				if(ret.next()) {
					f.totalGems = (int)ret.getLong("gemTotal");
					f.totalTickets = (int)ret.getLong("ticketTotal");
				}
				ret = SQLAccess.executeSelect("SELECT vote, COUNT(*) cc FROM (SELECT DISTINCT bannerHash, vote FROM Vote_User_Data WHERE userId = ?) a GROUP BY vote", userId);
				while(ret.next()) {
					switch(ret.getString("vote")) {
						case "SKIP": f.skipCount = (int)ret.getLong("cc"); break;
						case "TICKET": f.ticketCount = (int)ret.getLong("cc"); break;
						case "GEM": f.gemCount = (int)ret.getLong("cc"); break;
						case "LD": f.ldPityCount = (int)ret.getLong("cc"); break;
						case "BT": f.btPityCount = (int)ret.getLong("cc"); break;
					}
				}
				return f;
			} catch (BotException | SQLException e) {
				throw new BotException(e, BotException.ERROR_CODE);
			} 
		}
		public static Friend getFriendByCode(Integer code, String region) throws BotException {
			try {
				ResultSet ret = SQLAccess.executeSelect("SELECT * FROM Friend_Codes WHERE code = ? AND region = ?", ""+code, region);
				Friend f = new Friend();
				if(!ret.next())
					return null;
				f.userId = ret.getLong("userId");
				f.region = ret.getString("region");
				f.fcCode = ret.getString("code");
				f.unit = ret.getString("unit");
				f.note = ret.getString("note");
				f.ffootipId = ret.getString("ffootipId");
				ret = SQLAccess.executeSelect("SELECT SUM(gemCount) gemTotal, SUM(ticketCount) ticketTotal FROM User_Banner_Costs WHERE userId = ?", f.userId);
				if(ret.next()) {
					f.totalGems = (int)ret.getLong("gemTotal");
					f.totalTickets = (int)ret.getLong("ticketCount");
				}
				ret = SQLAccess.executeSelect("SELECT vote, COUNT(*) cc FROM (SELECT DISTINCT bannerHash, vote FROM Vote_User_Data WHERE userId = ?) a GROUP BY vote", f.userId);
				while(ret.next()) {
					switch(ret.getString("vote")) {
						case "SKIP": f.skipCount = (int)ret.getLong("cc"); break;
						case "TICKET": f.ticketCount = (int)ret.getLong("cc"); break;
						case "GEM": f.gemCount = (int)ret.getLong("cc"); break;
						case "LD": f.ldPityCount = (int)ret.getLong("cc"); break;
						case "BT": f.btPityCount = (int)ret.getLong("cc"); break;
					}
				}
				return f;
			} catch (BotException | SQLException e) {
				throw new BotException(e, BotException.ERROR_CODE);
			} 
		}
		public static List<Friend> getFriends(String region, String unit) throws BotException {
			try {
				ResultSet ret = SQLAccess.executeSelect("SELECT * FROM Friend_Codes WHERE region = ? AND unit = ?", region, unit);
				List<Friend> rett = new LinkedList<>();
				while(ret.next()) {
					Friend f = new Friend();
					f.userId = ret.getLong("userId");
					f.region = ret.getString("region");
					f.fcCode = ret.getString("code");
					f.unit = ret.getString("unit");
					f.note = ret.getString("note");
					f.ffootipId = ret.getString("ffootipId");
					rett.add(f);
				}
				return rett;
			} catch (BotException | SQLException e) {
				throw new BotException(e, BotException.ERROR_CODE);
			} 
		}
		public static void setFriend(long userId, String region, Integer fcCode, String unit, String note, String ffootip) throws BotException {
			Friend get = getFriend(userId, region);
			if(get != null) {
				if(get.userId != userId)
					throw new BotException("You cannot update a unit for a code that isn't yours!", BotException.INFO_CODE);
				if(ffootip == null)
					ffootip = get.ffootipId;
			}
			if(fcCode == null && unit == null) { //Delete
				int res = SQLAccess.executeInsert("DELETE FROM Friend_Codes WHERE userId = ? AND region = ?", userId, region);
				if(res == 0)
					throw new BotException("You don't have a friend code associated!", BotException.INFO_CODE);
			}
			else {
				if(fcCode == null) { 			//Update unit
					if(get == null)
						throw new BotException("You must associate a friend id before setting your unit!", BotException.INFO_CODE);
					SQLAccess.executeInsert("UPDATE Friend_Codes SET unit = ?, note = ?, ffootipId = ? WHERE userId = ? AND region = ?", unit, note, ffootip, userId, region);
				}
				else if(unit == null) 			//Only set code
					SQLAccess.executeInsert("REPLACE INTO Friend_Codes VALUES(?, ?, ?, ?, ?, ?);", userId, region, fcCode, null, note, ffootip);
				else { 						//Set both
					Friend f = getFriendByCode(fcCode, region);
					if(f != null && f.userId != userId)
						throw new BotException("Another user has registered this code, if this is your code and someone stole it, please contact Quetz(don't be afraid, he doesn't bite)", BotException.INFO_CODE);
					SQLAccess.executeInsert("REPLACE INTO Friend_Codes VALUES(?, ?, ?, ?, ?, ?);", userId, region, fcCode, unit, note, ffootip);
				}
			}
		}
	}
	public abstract static class Event{ private Event() {}
		public static com.materiabot.GameElements.Event getEvent(String name, String region) throws BotException {
			try {
				ResultSet r = SQLAccess.executeSelect("SELECT * FROM Events WHERE name = ? AND region = ? ORDER BY startDate DESC", name, region);
				if(r.next()) {
					com.materiabot.GameElements.Event e = new com.materiabot.GameElements.Event();
					e.setId(r.getInt("id"));
					e.setName(r.getString("name"));
					e.setRegion(r.getString("region"));
					for(String u : r.getString("featuredUnits").split(";"))
						e.getUnits().add(u);
					e.setStartDate(r.getTimestamp("startDate"));
					e.setEndDate(r.getTimestamp("endDate"));
					ResultSet rr = SQLAccess.executeSelect("SELECT * FROM Event_Details WHERE eventId = ? ORDER BY type DESC, id ASC", e.getId());
					while(rr.next()) {
						EventLink ev = new EventLink();
						ev.setEventId(rr.getInt("eventId"));
						ev.setLinkId(rr.getLong("id"));
						ev.setText(rr.getString("text"));
						ev.setType(rr.getString("type"));
						ev.setUrl(rr.getString("link"));
						e.getLinks().add(ev);
					}
					return e;
				}
				return null;
			} catch (SQLException | BotException e) {
				throw new BotException("Error loading event " + name + " from " + region, e);
			}
		}
		public static boolean saveEvent(com.materiabot.GameElements.Event event) throws BotException {
			ResultSet result = SQLAccess.executeSelect("SELECT * FROM Events WHERE name = ? AND startDate >= ?", event.getName(), new Timestamp(System.currentTimeMillis()).toString());
			try {
				if(result.next()) 
					return false;
				String units = event.getUnits().size() > 1 ? event.getUnits().stream().reduce((s1, s2) -> s1 + ";" + s2).orElse(null) : null;
				SQLAccess.executeInsert("REPLACE INTO Events(name, region, featuredUnits, startDate, endDate) VALUES(?, ?, ?, ?, ?)", 
						event.getName(), event.getRegion(), 
						units,
						event.getStartDate().toString(), event.getEndDate().toString());
				return true;
			} catch (SQLException | BotException e) {
				throw new BotException(e);
			}
		}
		public static int updateEvent(com.materiabot.GameElements.Event event) throws BotException {
			ResultSet result = SQLAccess.executeSelect("SELECT * FROM Events WHERE name = ? AND startDate >= ?", event.getName(), new Timestamp(System.currentTimeMillis()).toString());
			try {
				if(!result.next()) 
					return 1;
				SQLAccess.executeInsert("REPLACE INTO Events(id, name, region, featuredUnits, startDate, endDate) VALUES(?, ?, ?, ?, ?, ?)", 
						result.getInt(1), event.getName(), event.getRegion(), 
						event.getUnits().stream().reduce((s1, s2) -> s1 + ";" + s2).orElse(null),
						event.getStartDate().toString(), event.getEndDate().toString());
				return 0;
			} catch (SQLException | BotException e) {
				throw new BotException(e);
			}
		}
		public static boolean deleteEvent(String name, String region) throws BotException {
			ResultSet result = SQLAccess.executeSelect("SELECT * FROM Events WHERE name = ? AND region = ? AND endDate >= ?", 
					name, region, new Timestamp(System.currentTimeMillis()).toString());
			try {
				if(result.next()) {
					SQLAccess.executeInsert("DELETE FROM Events WHERE id = ?", result.getInt("id"));
					return true;
				}
				else
					return false;
			} catch (SQLException | BotException e) {
				throw new BotException(e);
			}
		}
		public static List<com.materiabot.GameElements.Event> getCurrentAndFutureEvents(String region){
			try {
				ResultSet r = SQLAccess.executeSelect("SELECT * FROM Events WHERE region = ? AND endDate > ? ORDER BY startDate ASC", region, new Timestamp(System.currentTimeMillis()).toString());
				List<com.materiabot.GameElements.Event> list = new LinkedList<>();
				while(r.next()) {
					com.materiabot.GameElements.Event e = new com.materiabot.GameElements.Event();
					e.setId(r.getInt("id"));
					e.setName(r.getString("name"));
					e.setRegion(r.getString("region"));
					if(r.getString("featuredUnits") != null)
						for(String u : r.getString("featuredUnits").split(";"))
							e.getUnits().add(u);
					e.setStartDate(r.getTimestamp("startDate"));
					e.setEndDate(r.getTimestamp("endDate"));
//					ResultSet rr = SQLAccess.executeSelect("SELECT * FROM Event_Details WHERE eventId = ? ORDER BY type DESC, id ASC", e.getId());
//					while(rr.next()) {
//						EventLink ev = new EventLink();
//						ev.setEventId(rr.getInt("eventId"));
//						ev.setLinkId(rr.getLong("id"));
//						ev.setText(rr.getString("text"));
//						ev.setType(rr.getString("type"));
//						ev.setUrl(rr.getString("link"));
//						e.getLinks().add(ev);
//					}
					list.add(e);
				}
				com.materiabot.GameElements.Event maintenance = list.stream().filter(e -> e.getName().contains("Maintenance")).findAny().orElse(null);
				if(maintenance != null) {
					list.remove(maintenance);
					list.add(0, maintenance);
				}
				com.materiabot.GameElements.Event stream = list.stream().filter(e -> e.getName().contains("Stream")).findAny().orElse(null);
				if(stream != null) {
					list.remove(stream);
					list.add(0, stream);
				}
				return list;
			} catch (BotException | SQLException e) {
				e.printStackTrace();
				return new LinkedList<>();
			}
		}
		public static int saveEventLink(com.materiabot.GameElements.Event.EventLink link) throws BotException {
			try {
				ResultSet result = executeSelect("SELECT * FROM Event_Details WHERE eventId = ? AND text = ?", link.getEventId(), link.getText());
				if(result.next()) {
					if(link.getUrl().equalsIgnoreCase("delete")) {
						SQLAccess.executeInsert("DELETE FROM Event_Details WHERE eventId = ? AND text = ?", 
								link.getEventId(), link.getText());
						return 2;
					}else {
						SQLAccess.executeInsert("UPDATE Event_Details SET url = ? WHERE eventId = ? AND text = ?", 
								link.getUrl(), link.getEventId(), link.getText());
						return 1;
					}
				}else {
					if(!link.getUrl().equalsIgnoreCase("delete")) {
						SQLAccess.executeInsert("INSERT INTO Event_Details(eventId, text, type, link) VALUES(?, ?, ?, ?)", 
								link.getEventId(), link.getText(), link.getType(), link.getUrl());
						return 0;
					}else
						return 3;
				}
			} catch (BotException | SQLException e) {
				throw new BotException(e);
			}
		}
	}
	public static class UserCosts{
		public long userId;
		public int bannerHash;
		public int gemCount = 0, ticketCount = 0, totalGems = 0, totalTickets = 0;

		public static UserCosts getUserCosts(UserCosts uc) {
			return getUserCosts(uc.userId, uc.bannerHash);
		}
		public static UserCosts getUserCosts(long userId, int bannerHash) {
			try {
				UserCosts uc = new UserCosts();
				uc.userId = userId;
				uc.bannerHash = bannerHash;
				ResultSet r = SQLAccess.executeSelect("SELECT ubc2.bannerHash, ubc2.gemCount, ubc2.ticketCount, SUM(ubc.gemCount) totalGems, SUM(ubc.ticketCount) totalTickets "
														+ "FROM User_Banner_Costs ubc "
															+ "LEFT JOIN User_Banner_Costs ubc2 ON ubc.userId = ubc2.userId AND ubc2.bannerHash = ? "
														+ "WHERE ubc.userId = ?", bannerHash, userId);
				if(r.next()) {
					uc.gemCount = r.getInt("gemCount");
					uc.ticketCount = r.getInt("ticketCount");
					uc.totalGems = r.getInt("totalGems");
					uc.totalTickets = r.getInt("totalTickets");
				}
				return uc;
			} catch (BotException | SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		public static void saveUserCosts(UserCosts uc) {
			try {
				SQLAccess.executeInsert("REPLACE INTO User_Banner_Costs VALUES(?, ?, ?, ?);", uc.userId, uc.bannerHash, uc.gemCount, uc.ticketCount);
			} catch (BotException e) {
				e.printStackTrace();
			}
		}
	}
}