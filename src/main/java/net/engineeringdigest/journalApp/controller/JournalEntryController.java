package net.engineeringdigest.journalApp.controller;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
          User user =userService.findByUserName(userName);
         List<JournalEntry>all=user.getJournalEntries();
         if(all!=null && !all.isEmpty()){
             return new ResponseEntity<>(all,HttpStatus.OK);
         }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName){
        try {
            User user=userService.findByUserName(userName);
            journalEntryService.saveEntry(myEntry,userName);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> findById(@PathVariable ObjectId id){
     Optional<JournalEntry> journalEntry= journalEntryService.findById(id);
     if(journalEntry.isPresent()){
         return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
     }
     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{userName}/{id}")
    public ResponseEntity<?>  deleteJournalEntryById(@PathVariable ObjectId id, @PathVariable String userName){
      journalEntryService.deleteById(id,userName);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{userName}/{id}")
    public ResponseEntity<JournalEntry>  updateJournalById(@PathVariable ObjectId id,@RequestBody JournalEntry newEntry,@PathVariable String userName)
    {
        JournalEntry old= journalEntryService.findById(id).orElse(null);
        if(old!=null){
            old.setTitle(newEntry.getTitle()!=null?newEntry.getTitle():old.getTitle());
            old.setContent(newEntry.getContent()!=null?newEntry.getContent():old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
