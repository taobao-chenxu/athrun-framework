/* Athrun - Android automation testing Framework.
 Copyright (C) 2010-2012 TaoBao UI AutoMan Team

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation; either version 2
 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., HuaXing road, Hangzhou,China. 
 Email:taichan@taobao.com,shidun@taobao.com,bingyang.djj@taobao.com
*/
package org.athrun.android.framework.agent.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;

public class DBCommandRunner {

	private BasicDataSource ds;
	private Connection connection;

	public String run(String command) throws Exception {
		String cl = command.toLowerCase();
		
		if (cl.startsWith("select") || cl.startsWith("insert")
				|| cl.startsWith("update"))
			return execute(command);
		
		else {
			return connect(command);
		}
	}

	/*
	 * 添加者：苏英 日期：2011-12-20 功能：返回指定的记录的指定字段的值 用法：String
	 * result2=TmtsServerThread.execute(
	 * "DB: select issue,last_buy_time from lottery_issue where last_buy_time >sysdate and start_time<sysdate and lottery_type_id=2"
	 * + (char)18 + "0"+(char)18+"issue"); 注意：要用(char)18去割开SQL，记录条数，属性值
	 */

	private String execute(String command) throws Exception {
		String[] args = command.split("" + (char) 18);
		String _command = args[0];
		PreparedStatement prepareStatement = connection
				.prepareStatement(_command);
		ResultSet rs = prepareStatement.executeQuery();// execute(),executeBatch(),executeUpdate()
		StringBuilder sb = new StringBuilder();

		java.sql.ResultSetMetaData rsmd = prepareStatement.getMetaData();
		//
		int columnCount = rsmd.getColumnCount();
		//
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		Map<String, Object> data = null;
		String recordIndex = null;
		String attribute = null;
		
		if (args.length == 3) {
			recordIndex = args[1];
			attribute = args[2];
		}
		//

		while (rs.next()) {
			data = new HashMap<String, Object>();
			//
			for (int i = 1; i <= columnCount; i++) {
				data.put(rsmd.getColumnLabel(i),
						rs.getObject(rsmd.getColumnLabel(i)));
			}
			// 将遍历的记录加到Map（字段，值）的List中

			datas.add(data);
		}
		if (recordIndex != null && attribute != null) {
			String value = getAttributeRecord(datas, recordIndex, attribute);
			sb.append(value);
			sb.append("\n");
		}

		rs.close();
		prepareStatement.close();

		return sb.toString();
	}

	/*
	 * 
	 * 添加者：苏英 日期：2011-12-4 功能：不同数据库的连接 用法：command 格式Oracle
	 * "DB: eladmin:Stephon_Marbury@oracle.jdbc.driver.OracleDriver,jdbc:oracle:thin:@10.232.31.104:1521:ark"
	 * 格式 SQL
	 * "root:architect@mysql://10.232.27.4/tcc-20110314?characterEncoding=GBK"
	 */

	private String connect(String command) throws Exception {
		if (connection != null) {
			connection.close();
		}

		String namePass = command.split("@")[0];
		String name = namePass.split(":")[0];
		String password = namePass.split(":")[1];
		String connectionStr = command.substring(namePass.length() + 1);
		System.out.println("+++" + connectionStr);
		String driverClassName = connectionStr.split(",")[0];
		String connectionUrl = connectionStr.split(",")[1];
		ds = new BasicDataSource();
		System.out.print(driverClassName + "--" + connectionUrl + "--" + name
				+ "--" + password);
		ds.setDriverClassName(driverClassName);
		ds.setUrl(connectionUrl);
		ds.setUsername(name);
		ds.setPassword(password);

		connection = ds.getConnection();
		return null;
	}

	/*
	 * 添加者：苏英 日期：2011-12-20 功能：根据记录的Index和字段的属性取相应的值
	 */

	private String getAttributeRecord(List<Map<String, Object>> datas,
			String recordIndex, String attribute) {
		Map<String, Object> map = datas.get(Integer.parseInt(recordIndex));
		String value = (String) map.get(attribute.toUpperCase());
		return value;
	}
}
