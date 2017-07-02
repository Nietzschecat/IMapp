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
			//ȡ�ý����˵�ͨ���߳�
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			//������˷�����Ϣ
			oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendBuddyList(YQMessage m){
		try{
			//�������ݿ⣬���غ����б�˳��Ⱥ�б�
			String res=udao.getBuddy(m.getSender())+","+gdao.getGroup();
			System.out.println(res);
			//���ͺ����б��ͻ���
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
			//�������ݿ⣬���غ����б�˳��Ⱥ�б�
			String res=udao.getBuddy(m.getSender());
			System.out.println("��Ӻ��Ѻ���б�: "+res);
			//���ͺ����б��ͻ���
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
				System.out.println("���еĳ�Ա"+raccount);
				//��ֻ֧�������ߵ�Ⱥ��Ա������Ϣ
				if(ManageServerConClient.isOnline(raccount)){
					System.out.println("����Ⱥ��Ϣ��"+raccount);
					ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
					ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
					//ֻ��ı�����ߺͷ�������Ϣ
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
