package org.athrun.ios.instrumentdriver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.athrun.ios.instrumentdriver.config.DriverUtil;

/**
 * @author ziyu.hch
 * 
 */
public class MySocket {

	/* instrument 接收返回的命令，几种处理类型的枚举 */
	enum ReturnedType {
		voidType, stringType, booleanType, numberType, JSONObject, JSONArray, exitType
	}

	private static int TIMEOUT_TIME = DriverUtil.getTimeOut() * 1000;

	public static ServerSocket server = null;

	public static void sendExit() throws Exception {

		exit(ReturnedType.exitType.toString());
	}

	private static void exit(String exitMark) throws Exception {
		startSocket();
		String request = null;
		try {
			Socket socket = null;
			socket = server.accept();
			BufferedReader is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			PrintWriter os = new PrintWriter(socket.getOutputStream());
			request = is.readLine();

			System.out.println("Client request  : " + request);
			System.out.println("Server response : " + exitMark + "  -End-");

			os.print(exitMark);
			os.flush();

			os.close();
			is.close();
			socket.close();

			if (request.startsWith("Exception")) {
				System.err.println(request);
				os.close();
				is.close();
				socket.close();
				throw new Exception(request);
			}
		} catch (SocketTimeoutException te) {
			te.printStackTrace();
			System.out.println("Socket连接超时，服务端客户端通信中断");
			throw te;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getJSONArray(String script) throws Exception {
		String guids = send(ReturnedType.JSONArray + "##" + script);
		return guids;
	}

	public static String getJSONObject(String script) throws Exception {
		String guids = send(ReturnedType.JSONObject + "##" + script);
		return guids;
	}

	public static String getText(String script) throws Exception {
		return send(ReturnedType.stringType + "##" + script);
	}

	public static int getInt(String script) throws Exception {
		try {
			return Integer.parseInt(send(ReturnedType.numberType + "##"
					+ script));
		} catch (NumberFormatException e) {
			return 0;
		} catch (Exception e) {
			throw e;
		}
	}

	public static Boolean getBoolen(String script) throws Exception {
		String returnStr = send(ReturnedType.booleanType + "##" + script);
		if (returnStr.equals("1")) {
			return true;
		} else {

			return false;
		}
	}

	public static void getVoid(String script) throws Exception {
		send(ReturnedType.voidType + "##" + script);
	}

	public static void startSocket() throws Exception {
		if (server == null || server.isClosed()) {
			try {
				System.out.println("The server listen to port: 5566");
				server = new ServerSocket();
				server.setSoTimeout(TIMEOUT_TIME);
				server.setReuseAddress(true);
				server.bind(new InetSocketAddress(5566));
				// 创建一个ServerSocket在端口5566监听客户请求
			} catch (Exception e) {
				throw new Exception("can not listen to:" + e);
			}
		}
	}

	public static void tearDownSocket() throws Exception {
		if (server != null) {
			try {
				server.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			server = null;
		}
	}

	private static String send(String script) throws Exception {
		startSocket();
		String guid = null;
		String request = null;
		Socket socket = null;
		BufferedReader is = null;
		PrintWriter os = null;

		try {
//			socket = server.accept();
//			is = new BufferedReader(new InputStreamReader(
//					socket.getInputStream()));
//			os = new PrintWriter(socket.getOutputStream());

			////modified by zhijie
			socket = server.accept();
			is = new BufferedReader(new InputStreamReader(
			socket.getInputStream(), "UTF-8"));
			os = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			////modified by zhijie end

			request = is.readLine();

			// 用例执行错误的时候，获取到发回的异常信息并抛出
			if (request.startsWith("Exception")) {
				System.out.println(request);
				os.close();
				is.close();
				socket.close();
				throw new Exception(request);
			}
			System.out.println("Client request  : " + request);
			System.out.println("Server response : " + script);

			os.print(script);
			os.flush();

			os.close();
			is.close();
			socket.close();

			// 第二次建立socket，获取 上一步运行的结果
			socket = server.accept();

			is = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			guid = is.readLine();

			// 用例执行错误的时候，获取到发回的异常信息并抛出
			if (guid.startsWith("Exception")) {
				System.out.println(guid);
				os.close();
				is.close();
				socket.close();
				throw new Exception(guid);
			}
			System.out.println("Client response : " + guid);
			System.out
					.println("Server request  : Case step executed. Please request the next step.");

			is.close();
			socket.close();

		} catch (SocketTimeoutException te) {
			te.printStackTrace();
			System.out.println("Socket连接超时，服务端客户端通信中断");
			throw te;
		} catch (Exception e) {
			throw e;
		}
		return guid;
	}
}
