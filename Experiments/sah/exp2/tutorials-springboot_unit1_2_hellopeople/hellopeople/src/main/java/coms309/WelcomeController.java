package coms309;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Rohan Sah
 */

@RestController
class WelcomeController {
    private final HashMap<Long, String> names = new HashMap<>();

    @GetMapping("/309")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    //Path Variable(Read)
    @GetMapping("/{name}/{age}/{favoritefood}")
    public String read(@PathVariable String name, @PathVariable String age, @PathVariable String favoritefood) {
        return "Hello " + name + "You are " + age + "years old" + "You like " + favoritefood;
    }

    //Parameters(Read)
    @GetMapping("/")
    public String todolist(@RequestParam String name) {
        return "Todo list for " + name;
    }

    @GetMapping("/names/{id}")
    public String getnamebyID(@PathVariable Long id)
    {
        String namebyID = names.get(id);
        if(namebyID != null)
        {
            return "Hey, " + namebyID;
        }
        return "id does not exist!";

    }

    //Request Param stuff(Create)
    @PostMapping("/name")
    public String createname(@RequestParam String name, @RequestParam Long id) {
        if (!names.containsKey(id))
        {
            names.put(id, name);
            return "name created: " + name + "with corresponding id: " + id;
        }
        return "id already exists! pick new one!";
    }

    //Update(Request Param stuff)
    @PutMapping("/name/{id}")
    public String updatename(@PathVariable Long id, @RequestBody String newname)
    {
        if(names.containsKey(id))
        {
            names.put(id, newname);
            return "name uptated to: " + newname;
        }
        return "id does not exist!";

    }



}
