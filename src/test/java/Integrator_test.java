import integrator.integrator;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Integrator_test {
    private static final double DELTA = 1e-15;
    integrator integ = new integrator();

    @Test
    public void save_load_test() throws IOException {
        ArrayList<integrator.virtual_machine> virtual_machines = new ArrayList<>();
        Dictionary<String, integrator.trigger> triggers = new Hashtable<>();
        ArrayList<integrator.virtual_machine> virtual_machines_load = new ArrayList<>();
        Dictionary<String, integrator.trigger> triggers_load = new Hashtable<>();

        virtual_machines.add(new integrator.virtual_machine("0.0.0.0","vm_1"));
        virtual_machines.add(new integrator.virtual_machine("0.0.0.1","vm_2"));

        triggers.put("0",new integrator.trigger("asjkadjshj","trigger 1"));
        triggers.put("1",new integrator.trigger("sjdhfkshdf","Trigger 2"));

        integ.save_data(virtual_machines, triggers);
        integ.load_data(virtual_machines_load, triggers_load);

        assertEquals(virtual_machines.get(0).get_ip(), virtual_machines_load.get(0).get_ip());
        assertEquals(virtual_machines.get(1).get_name(),virtual_machines_load.get(1).get_name());
        assertEquals(triggers.get(String.valueOf(1)).get_hook(),
                triggers_load.get(String.valueOf(1)).get_hook());
        assertEquals(triggers.get(String.valueOf(0)).get_name(),
                triggers_load.get(String.valueOf(0)).get_name());
    }
}

