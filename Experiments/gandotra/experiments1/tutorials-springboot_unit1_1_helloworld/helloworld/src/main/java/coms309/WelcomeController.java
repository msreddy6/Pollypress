package coms309;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
class WelcomeController {

    private String name;

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public String create(@RequestBody String name)
    {
        return "Hi whats going on" + name;
    }

    //Path Variable
    @GetMapping("/{name}")
    public String read(@PathVariable String name) {
        return "Hi my name is " + name;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public String update(@RequestBody String name)
    {
        return "This is the update method " + name;
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long age)
    {
        return "You are " + age + " years old";
    }



}
