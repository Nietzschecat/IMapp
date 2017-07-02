package org.bmd.yq.server.model;
import java.io.ObjectOutputStream;
import java.util.List;

import org.bmd.yq.common.YQMessage;
import org.bmd.yq.common.YQMessageType;
import org.bmd.yq.server.dao.GroupDao;
import org.bmd.yq.server.dao.UserDao;

public class DoWhatAndSendMes {
	static UserDao udao=new UserDao();
	static GroupDao gdao=new GroupDao();
	
	public static void sendMes(YQMessage m){
		try{
			//取得接收人的通信线程
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			//向接收人发送消息
			oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendBuddyList(YQMessage m){
		try{
			//操作数据库，返回好友列表，顺带群列表
			String res=udao.getBuddy(m.getSender())+","+gdao.getGroup();
			System.out.println(res);
			//发送好友列表到客户端
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			YQMessage ms=new YQMessage();
			ms.setType(YQMessageType.RET_ONLINE_FRIENDS);
			ms.setContent(res);
			oos.writeObject(ms);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void sendRefreshBuddy(YQMessage m){
		try{
			//操作数据库，返回好友列表，顺带群列表
			String res=udao.getBuddy(m.getSender());
			System.out.println("添加好友后的列表: "+res);
			//发送好友列表到客户端
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			YQMessage ms=new YQMessage();
			ms.setType(YQMessageType.REFRESH_BUDDY);
			ms.setContent(res);
			oos.writeObject(ms);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void delBuddy(YQMessage m){
		try{
			if(udao.delBuddy(m.getSender(), m.getReceiver())){
				ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
				ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
				YQMessage ms=new YQMessage();
				ms.setType(YQMessageType.SUCCESS);
				oos.writeObject(ms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void AddBuddy(YQMessage m){
		try{
			if(udao.AddBuddy(m.getSender(), m.getReceiver())){
				ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
				ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
				YQMessage ms=new YQMessage();
				ms.setType(YQMessageType.SUCCESS);
				oos.writeObject(ms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendGroupMes(YQMessage m){
		try{
			List<Integer> list=gdao.getGroupMember(m.getReceiver());
			for(int raccount:list){
				System.out.println("所有的成员"+raccount);
				//暂只支持向在线的群成员发送消息
				if(ManageServerConClient.isOnline(raccount)){
					System.out.println("发送群消息给"+raccount);
					ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
					ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
					//只需改变接收者和发送者信息
					m.setType(YQMessageType.GROUP_MES);
					m.setSender(m.getReceiver());
					m.setReceiver(raccount);
					oos.writeObject(m);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
