import java.net.*;
import java.io.*;
import java.util.*;
public class ClientDataHost implements Runnable {
    ServerSocket ss;
    Socket s;
    BufferedReader din;
    DataOutputStream dout;
    InputStream is;
    ArrayList<String> chat;
    PrintStream ps;
    boolean connected;
    ArrayList<PlayerLocation> playerlocs;
    long mapseed;
    public static void main(String[] args) throws Exception {
        new ClientDataHost();
    }
    public ClientDataHost() {
        chat = new ArrayList<String>();
        connected=false;
        playerlocs = new ArrayList<PlayerLocation>();
        mapseed=0;
        System.out.println("ClientDataHost construction complete");
    }
    public void run() {
        Socket s = null;
        try {
            //s = new Socket("10.7.65.45",1600);
            s = new Socket("71.115.226.213",1600);
            ps = new PrintStream(s.getOutputStream());
            connected=true;
            //InetAddress IP=InetAddress.getLocalHost();
            //String IPaddr = IP.getHostAddress();
            //sps.println("IPaddr");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (s==null) {
            System.out.println("An error occured establishing a connection with the server");
            connected=false;
            //System.exit(1);
        }
        /*try {
            ss = new ServerSocket(1601);
            //din = new BufferedReader(new InputStreamReader(ss.getInputStream()));
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            //s = ss.accept();
            din = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println("server connection achieved " + s.getInetAddress());
            //(new Thread(new serverRunabble(s))).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(true) {
            //System.out.println("running cdh loop");
            String line = "";
            try {
                line = din.readLine();
                process(line);
                //chat.add(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println("server input received: " + line);
        }
    }
    public ArrayList<String> getChat() {
        return chat;
    }
    public ArrayList<PlayerLocation> getPlayers() {
        return playerlocs;
    }
    public ArrayList<String> getRecent() {
        ArrayList<String> temp = new ArrayList<String>();
        int start=0;
        int amount=22;
        if (chat.size()-amount>=0) {
            start=chat.size()-amount;
        } else {
            start=0;
        }
        for(int i=start;i<chat.size();i++) {
            temp.add(chat.get(i));
        }
        return temp;
    }
    public void process(String input) {
        /*Structure of a server command:
         * t1:t2:s1:s2:s3:s4:s5
         * t1 is type 1(chat,location,mapdata), t2 is type 2(more unique commands)
         * s1,s2,s3,s4,s5, are strings that can be sent optionally
        */
        if (input.indexOf(":")<0) {
            //System.out.println("broken input: " + input);
            return;
        }
       
        String[] components = new String[7];
        for(int i=0;i<7;i++) {
            if (i<6) {
                components[i]=input.substring(0,input.indexOf(":"));
                input=input.substring(input.indexOf(":")+1);
            } else {
                components[i]=input;
            }
        }
        /*Structure of a chat message:
         * c:n:name:message:n:n:n
         * c=chat
         * n=null
         * name=name of sender
         * message=the text content
        */
        for(String s : components) {
            //System.out.println(s);
        }
        if (components[0].equals("c")) {
            System.out.println("chat message received!");
            String full = components[2]+": "+components[3];
            if (full.length()>30) {
                for(int i=0;i<full.length();i+=30) {
                    if (full.length()<i+30) {
                        chat.add(full.substring(i));
                    } else { 
                        chat.add(full.substring(i,i+30));
                    }
                }
            } else {
                chat.add(full);
            }
        }
        /*Structure of a player location:
         *p:l:name:xcord:ycord:n:n
         *p=player
         *l=location
         *name=name of other player
         *xcord=x cord of the player
         *ycord=y cord of the player
         *n=null
         */
        if(components[0].equals("p")) {
            if(components[1].equals("l")) {
                String nm=components[2];
                int xc=Integer.parseInt(components[3]);
                int yc=Integer.parseInt(components[4]);
                boolean f = false;
                for(int i=0;i<playerlocs.size();i++) {
                    PlayerLocation pl = playerlocs.get(i);
                    if ((pl.n).equals(nm)) {
                        f=true;
                        playerlocs.set(i,new PlayerLocation(pl.n,xc,yc));
                    }
                }
                if (!f) {
                    playerlocs.add(new PlayerLocation(nm,xc,yc));
                }
                
                //playerlocs.add(new PlayerLocation(nm,xc,yc));
            }
        }
        /*Structure of a map initiation:
         * m:s:seed:n:n:n:n
         * m=map
         * s=seed
         * seed=actual seed number
         * n=null
         */
        if(components[0].equals("m")) {
            if(components[1].equals("s")) {
                mapseed=Long.parseLong(components[2]);
            }
        }
    } 
    public static String encodeChat(String name, String message) {
        return ("c:n:"+name+":"+message+":n:n:n");
    }
    public static String encodePlayerLocation(String name, int x, int y) {
        return ("p:l:"+name+":"+x+":"+y+":n:n");
    }
    public void sendMessage(String name,String message) {
        //chat.add(name+": "+message);
        if (connected) {
            ps.println(encodeChat(name,message));
            ps.flush();
        } else {
            //chat.add(name+": "+message);
            String full = name+": "+message;
            if (full.length()>30) {
                for(int i=0;i<full.length();i+=30) {
                    if (full.length()<i+30) {
                        chat.add(full.substring(i));
                    } else { 
                        chat.add(full.substring(i,i+30));
                    }
                }
            } else {
                chat.add(full);
            }
        }
    }
    public void sendPlayerLocation(String name, int x, int y) {
        if (connected) {
            ps.println(encodePlayerLocation(name,x,y));
            ps.flush();
        } 
    }
    public long getMapSeed() {
        if (connected) {
            while(mapseed==0) {}
            return mapseed;
        }
        return (int)(Math.random()*100)*(int)(Math.random()*100)*(int)(Math.random()*100);
    }
}