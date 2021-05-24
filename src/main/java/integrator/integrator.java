package integrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;


public class integrator {
    public static final Logger logger = LogManager.getLogger(integrator.class);

    public integrator() {}

    public static void main(String[] args) throws IOException {
        //Scanner scanner = new Scanner(System.in);
        ArrayList<virtual_machine> virtual_machines = new ArrayList<>();
        Dictionary<String, trigger> triggers = new Hashtable<>();

        virtual_machine vm_1 = new virtual_machine("0.0.0.0","vm_1");
        logger.info("new vm added with ip: " + vm_1.ip);
        virtual_machine vm_2 = new virtual_machine("0.0.0.1","vm_2");
        logger.info("new vm added with ip:" + vm_2.ip);

        trigger trig_1 = new trigger("asjkadjshj","trigger 1");
        logger.info("new trigger added with hook", trig_1.hook);
        trigger trig_2 = new trigger("sjdhfkshdf","Trigger 2");
        logger.info("new trigger added with hook", trig_2.hook);

        virtual_machines.add(vm_1);
        virtual_machines.add(vm_2);

        triggers.put("0",trig_1);
        triggers.put("1",trig_2);

        save_data(virtual_machines,triggers);
        load_data(virtual_machines,triggers);
        ArrayList<Integer> lst = new ArrayList<>();
        lst.add(1);
        lst.add(0);
        lst.add(2);
        lst.add(4);
        build_yaml(lst,"auto","This job is created aby magic.","local");

    }


    static class rundeck_commands{
        public rundeck_commands(ArrayList<Integer> tags) {
            for (Integer tag : tags) {
                switch (tag) {
                    case 0:
                        Jdk8();
                        this.all.addAll(getJdk_8());
                        break;
                    case 1:
                        Jenkins();
                        this.all.addAll(getJenkins());
                        break;
                    case 2:
                        run_d();
                        this.all.addAll(getRun_d());
                        break;
                    case 3:
                        k8();
                        this.all.addAll(getKube());
                        break;
                    case 4:
                        k8_gui();
                        this.all.addAll(getKube());
                        break;
                }
            }
        }
        ArrayList<String> jdk_8 = new ArrayList<>();
        ArrayList<String> jenkins = new ArrayList<>();
        ArrayList<String> run_d = new ArrayList<>();
        ArrayList<String> kube = new ArrayList<>();
        ArrayList<String> all = new ArrayList<>();

        void Jdk8() {
            this.jdk_8.add("exec: sudo apt update");
            this.jdk_8.add("exec: sudo apt install openjdk-8-jdk -y");
            this.jdk_8.add("exec: export PATH="+'"'+"usr/lib/jvm/java-8-openjdk-amd64/bin"+'"');
            this.jdk_8.add("exec: source /etc/environment");
        }

        void Jenkins() {
            this.jenkins.add("exec: wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo apt-key add -");
            this.jenkins.add("exec: sudo sh -c 'echo deb http://pkg.jenkins.io/debian-stablebinary/ >/etc/apt/sources.list.d/jenkins.list'");
            this.jenkins.add("exec: sudo apt update");
            this.jenkins.add("exec: sudo apt install jenkins -y");
            this.jenkins.add("exec: sudo service jenkins start");
            this.jenkins.add("exec: POST(cat /var/lib/jenkins/secrets/initialAdminPassword)"); //implement api
        }

        void run_d(){
            this.run_d.add("exec: echo \"deb https://rundeck.bintray.com/rundeck-deb/\" | sudo tee -a/etc/apt/sources.list.d/rundeck.list");
            this.run_d.add("exec: curl 'https://bintray.com/user/downloadSubjectPublicKey?username=bintray'| sudo apt-key add -");
            this.run_d.add("exec: sudo apt update");
            this.run_d.add("exec: sudo apt install rundeck -y");
            this.run_d.add("exec: sudo service rundeckd start");
        }

        void k8(){
            this.kube.add("exec: curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64");
            this.kube.add("exec: sudo install minikube-linux-amd64 /usr/local/bin/minikube");
            this.kube.add("exec: minikube start");
        }
        void k8_gui(){
            this.kube.add("exec: kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.2.0/aio/deploy/recommended.yaml");
            this.kube.add("exec: kubectl proxy");
        }

        public ArrayList<String> getJdk_8(){
            return this.jdk_8;
        }

        public ArrayList<String> getJenkins(){
            return this.jenkins;
        }

        public ArrayList<String> getRun_d(){
            return this.run_d;
        }

        public ArrayList<String> getKube(){
            return this.kube;
        }

        public ArrayList<String> getall(){ return this.all; }
    }

    static class rundeck{
        public rundeck() {}

        public rundeck(String name,
                       String desc,
                       String exec_enable,
                       String log_level,
                       String node_filters,
                       String node_exclude_precedence,
                       String node_keep_going,
                       String node_rank_order,
                       String node_success,
                       String node_threads,
                       String node_filter,
                       ArrayList<Integer> commands_list,
                       String sequence_keep_going,
                       String sequence_strategy){

            rundeck_commands rnd_cmd = new rundeck_commands(commands_list);

            this.name = "name: " + name;
            this.desc = "description: " + desc;
            this.exec_enable = "executionEnabled: " + exec_enable;
            this.log_level = "loglevel: " + log_level;
            this.node_filters = "nodeFilterEditable: " + node_filters + "\nnodefilters:\n  dispatch:";
            this.node_exclude_precedence = "    excludePrecedence: " + node_exclude_precedence;
            this.node_keep_going = "    keepgoing: " + node_keep_going;
            this.node_rank_order = "    rankOrder: " + node_rank_order;
            this.node_success = "    successOnEmptyNodeFilter: " + node_success;
            this.node_threads = "    threadcount: " + node_threads;
            this.node_filter = "  filter: " + node_filter;
            this.sequence = "sequence: " + "\n  commands:";
            this.exec = rnd_cmd.getall();
            this.sequence_keep_going = sequence_keep_going;
            this.sequence_strategy = sequence_strategy;
        }
        public String name;
        public String desc;
        public String exec_enable;
        public String log_level;
        public String node_filters; //node filter editable
        public String node_exclude_precedence;
        public String node_keep_going;
        public String node_rank_order;
        public String node_success;
        public String node_threads;
        public String node_filter;
        public String sequence;
        public ArrayList<String> exec;
        public String sequence_keep_going;
        public String sequence_strategy;

    }

    static void build_yaml(ArrayList<Integer> list, String name, String desc, String node_filter) throws IOException {
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        rundeck rdk = new rundeck(name,
                desc,
                "true",
                "INFO",
                "false",
                "true",
                "false",
                "ascending",
                "false",
                "'"+"1"+"'",
                node_filter,
                list,
                "false",
                "node-first");

        om.writeValue(new File("./rundeck.yaml"),rdk);
        logger.info("rundeck yaml file created with parameters:"+name+list);
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

    public static void load_data(ArrayList<virtual_machine> vm_list, Dictionary<String, trigger> trigger_dic) throws FileNotFoundException {

        JSONObject list = new JSONObject(file_string("vm_list.json"));
        for (int i =0; i < list.length(); i++){
            vm_list.add(new virtual_machine(list.getJSONObject(String.valueOf(i)).getString("ip"), list.getJSONObject(String.valueOf(i)).getString("user_name")));
        }
        logger.info("virutal machine details loaded");

        list = new JSONObject(file_string("triggers.json"));
        for (int i =0; i < list.length(); i++){
            trigger_dic.put(String.valueOf(i), new trigger(list.getJSONObject(String.valueOf(i)).getString("hook"), list.getJSONObject(String.valueOf(i)).getString("name")));
        }
        logger.info("Trigger details loaded");
    }

    public static void save_data(ArrayList<virtual_machine> vm_list, Dictionary<String, trigger> trigger_dic) throws IOException {
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
        logger.info("Virtual machines details saved");

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
        logger.info("Trigger details saved");
    }

    public static class virtual_machine {
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

    public static class trigger {
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
