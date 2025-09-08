package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.JournalEntry;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    private Map<Long, JournalEntry> journalEntries=new HashMap<>();

    @GetMapping
    public List<JournalEntry>getAll(){
        return new ArrayList<>(journalEntries.values());
    }

    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry){
        journalEntries.put(myEntry.getId(),myEntry);
        return true;
    }

    @GetMapping("id/{id}")
    public JournalEntry findById(@PathVariable Long id){
       return journalEntries.get(id);
    }

    @DeleteMapping("/id/{id}")
    public JournalEntry deleteJournalEntryById(@PathVariable Long id){
        return journalEntries.remove(id);
    }

    @PutMapping
    public JournalEntry updateJournalById(@PathVariable Long id,@RequestBody JournalEntry myEntry){
        return journalEntries.put(id,myEntry);
    }

}
