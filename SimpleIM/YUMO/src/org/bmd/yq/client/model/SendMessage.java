package org.bmd.yq.client.model;
import java.io.ObjectOutputStream;

import org.yhn.bmd.client.view.MoreActivity;
import org.yhn.bmd.common.MyTime;
import org.yhn.bmd.common.YQMessage;

public class SendMessage {
	public static void sendMes(int dfAccount,String content,String type){
		try{
			int myAccount=MoreActivity.me.getAccount();
			ObjectOutputStream oos = new ObjectOutputStream
			//ͨ��account�ҵ����̣߳��Ӷ��õ�OutputStream
			(ManageClientConServer.getClientConServerThread(myAccount).getS().getOutputStream());
			//������Ϣ
			YQMessage m=new YQMessage();
			m.setType(type);
			m.setSender(myAccount);
			m.setSenderNick(MoreActivity.me.getNick());
			m.setSenderAvatar(MoreActivity.me.getAvatar());
			m.setReceiver(dfAccount);
			m.setContent(content);
			m.setSendTime(MyTime.geTimeNoS());
			oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendADbuddy(int myAccount,int dfAccount ,String type){
		try{
			ObjectOutputStream oos = new ObjectOutputStream
			(ManageClientConServer.getClientConServerThread(myAccount).getS().getOutputStream());
			YQMessage m=new YQMessage();
			m.setType(type);
			m.setSender(myAccount);
			m.setReceiver(dfAccount);
			oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
