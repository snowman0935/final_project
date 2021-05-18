package integrator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.FileUtils;
import org.json.JSONObject;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


public class integrator {
    public static final Logger logger = LogManager.getLogger(integrator.class);

    public integrator() {}

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<virtual_machine> virtual_machines = new ArrayList<>();
        Dictionary<String, trigger> triggers = new Hashtable<>();

        virtual_machine vm_1 = new virtual_machine("0.0.0.0","vm_1");
        virtual_machine vm_2 = new virtual_machine("0.0.0.1","vm_2");

        trigger trig_1 = new trigger("asjkadjshj","trigger 1");
        trigger trig_2 = new trigger("sjdhfkshdf","Trigger 2");

        virtual_machines.add(vm_1);
        virtual_machines.add(vm_2);

        triggers.put("0",trig_1);
        triggers.put("1",trig_2);

        save_data(virtual_machines,triggers);
        load_data(virtual_machines, triggers);

    }


    static class rundeck_commands{
        public rundeck_commands() {}
        ArrayList<String> jdk = new ArrayList<>();
        jdk.add("sudo apt update");

    }

    static class rundeck{
        public rundeck() {}

        public rundeck(String name, String description){

        }

        private String name;
        private String desc;
        private String node_name;

    }

    static void build(ArrayList<String> list, String addr){

    }

    static String file_string(String name){
        File f = new File(name);
        try {
            byte[] bytes = Files.readAllBytes(f.toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "-1";
        }
    }

    static void load_data(ArrayList<virtual_machine> vm_list, Dictionary<String, trigger> trigger_dic) throws FileNotFoundException {

        JSONObject list = new JSONObject(file_string("vm_list.json"));
        for (int i =0; i < list.length(); i++){
            vm_list.add(new virtual_machine(list.getJSONObject(String.valueOf(i)).getString("ip"), list.getJSONObject(String.valueOf(i)).getString("user_name")));
        }

        list = new JSONObject(file_string("triggers.json"));
        for (int i =0; i < list.length(); i++){
            trigger_dic.put(String.valueOf(i), new trigger(list.getJSONObject(String.valueOf(i)).getString("hook"), list.getJSONObject(String.valueOf(i)).getString("name")));
        }
    }

    static void save_data(ArrayList<virtual_machine> vm_list, Dictionary<String, trigger> trigger_dic ) throws IOException {
        JSONObject vm = new JSONObject();
        JSONObject trig = new JSONObject();

        for (int i = 0; i < vm_list.size(); i++) {
            Map<String, String> m = new HashMap<>(2);
            m.put("user_name", vm_list.get(i).get_name());
            m.put("ip", vm_list.get(i).get_ip());
            vm.put(String.valueOf(i), m);
        }
        FileWriter file = new FileWriter("vm_list.json");
        try {
            file.write(vm.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }

        for (int i = 0; i < trigger_dic.size(); i++){
            Map<String, String> m = new HashMap<>(2);
            m.put("name",trigger_dic.get(String.valueOf(i)).get_name());
            m.put("hook",trigger_dic.get(String.valueOf(i)).get_hook());
            trig.put(String.valueOf(i),m);
        }
        try{
            file = new FileWriter("triggers.json");
            file.write(trig.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.flush();
            file.close();
        }
    }

    static class virtual_machine {
        String ip;
        String user_name;

        public virtual_machine(String Ip, String Name){
            this.ip = Ip;
            this.user_name =Name;
        }

        public String get_ip(){
            return this.ip;
        }

        public String get_name(){
            return this.user_name;
        }
    }

    static class trigger {
        String hook;
        String name;

        public trigger(String Hook, String Name){
            this.hook = Hook;
            this.name = Name;
        }

        public String get_name(){
            return this.name;
        }

        public String get_hook(){
            return this.hook;
        }
    }



}
