package com.techelevator.controller;


import com.techelevator.dao.CatCardDao;
import com.techelevator.model.CatCard;
import com.techelevator.model.CatCardNotFoundException;
import com.techelevator.model.CatFact;
import com.techelevator.model.CatPic;
import com.techelevator.services.CatFactService;
import com.techelevator.services.CatPicService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CatController {

    private CatCardDao cat;
    private CatFactService catFact;
    private CatPicService catPic;

    public CatController(CatCardDao cat, CatFactService catFact, CatPicService catPic) {
        this.cat = cat;
        this.catFact = catFact;
        this.catPic = catPic;
    }
    //Provides a list of all Cat Cards in the user's collection.
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<CatCard> getAllCards() throws CatCardNotFoundException {
        return cat.list();
    }
    //Provides a Cat Card with the given ID.
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public CatCard getIndividualCard(@PathVariable long id) throws CatCardNotFoundException {
        return cat.get(id);
    }


    //Provides a new, randomly created Cat Card containing
    //information from the cat fact and picture services.
    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public CatCard makeNewCard() {
        CatFact f = catFact.getFact();
        CatPic p = catPic.getPic();
        CatCard c = new CatCard();
        c.setCatFact(f.getText());
        c.setImgUrl(p.getFile());
        return c;
    }

    //Saves a card to the user's collection.
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void saveNewCard(@Valid @RequestBody CatCard incomingCard) {
        cat.save(incomingCard);
    }

    //Updates a card in the user's collection
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateExistingCard(@Valid @RequestBody CatCard changedCard, @PathVariable long id) throws CatCardNotFoundException {
        cat.update(id, changedCard);
    }

    //Removes a card from the user's collection.
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteExistingCard(@PathVariable long id) throws CatCardNotFoundException {
        if(cat.get(id) != null) {
            cat.delete(id);
        }
    }
}
