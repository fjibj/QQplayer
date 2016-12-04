package com.scienjus.smartqq;

import com.scienjus.smartqq.callback.MessageCallback;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.*;

import java.io.IOException;
import java.util.List;

/**
 * @author ScienJus
 * @date 2015/12/18.
 */
public class Application {

    public static void main(String[] args) {
        String aa = "B.【全球风】";
        System.out.println(aa.indexOf("B"));
        System.out.println(aa.indexOf("【"));
        //创建一个新对象时需要扫描二维码登录，并且传一个处理接收到消息的回调，如果你不需要接收消息，可以传null
 /*       SmartQQClient client = new SmartQQClient(new MessageCallback() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message.getContent());
            }

            @Override
            public void onGroupMessage(GroupMessage message) {
                System.out.println(message.getContent());
            }

            @Override
            public void onDiscussMessage(DiscussMessage message) {
                System.out.println(message.getContent());
            }
        });
        //登录成功后便可以编写你自己的业务逻辑了
        System.out.println("-------------好友信息-------------------------");
        List<Category> categories = client.getFriendListWithCategory();
        for (Category category : categories) {
            System.out.println(category.getName());
            System.out.println("Nickname,UserId,Markname,VipLevel,QQ号");
            for (Friend friend : category.getFriends()) {
                System.out.println(friend.getNickname() + "," + friend.getUserId() + "," + friend.getMarkname() + "," + friend.getVipLevel() + "," + client.getQQById(friend));
            }
        }
        System.out.println("-------------群信息-------------------------");
        List<Group> groups = client.getGroupList();
        System.out.println("Name,Id,Code,Flag,QQ号");
        for (Group group : groups) {
            System.out.println(group.getName() + "," + group.getId() + "," + group.getCode() + "," + group.getFlag() + "," + client.getQQById(group.getId()));
        }
        System.out.println("-------------讨论组信息-------------------------");
        List<Discuss> discusses = client.getDiscussList();
        System.out.println("Name,Id");
        for (Discuss discuss : discusses) {
            System.out.println(discuss.getName() + ":" + discuss.getId());
        }*/
//        for (int i = 0; i < 10; i++)
//            client.sendMessageToFriend(3840860687l, "哦，My GOD！");

//        for (int i = 0; i < 10; i++)
//            client.sendMessageToGroup(4141534819l, "哈哈，哈哈，哈哈！终于成功了！");

        /*for (int i = 0; i < 10; i++)
            client.sendMessageToDiscuss(570333544, "Hello everyone! 你们好啊！");*/

        //使用后调用close方法关闭，你也可以使用try-with-resource创建该对象并自动关闭
      /*  try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
