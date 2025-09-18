package coms309;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.HttpConstraint;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Rohan Sah
 */

@RestController
class WelcomeController {

    private final Map<Long, String> names = new HashMap<>();

    @GetMapping("/309")
    @ResponseStatus(HttpStatus.OK)
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    //Path Variable(Read)
    @GetMapping("/{name}/{age}/{favoritefood}")
    @ResponseStatus(HttpStatus.OK)
    public String read(@PathVariable String name, @PathVariable String age, @PathVariable String favoritefood) {
        return "Hello " + name + "You are " + age + "years old" + "You like " + favoritefood;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public String read() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(names);
    }

    //Parameters(Read)
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String todolist(@RequestParam String name) {
        return "Todo list for " + name;
    }

    @GetMapping("/names/{id}")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.CREATED)
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
