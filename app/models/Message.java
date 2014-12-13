package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.*;

import com.avaje.ebean.*;
import com.avaje.ebean.annotation.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;

@Entity
public class Message extends Model {
	
	@Id
	public Long id;
	
	@Required
	public List<String> name;
	public String mail;
	
	@Required
	public String message;
	
	@ManyToMany(cascade = CascadeType.ALL)
	public List<Member> members = new ArrayList<Member>();
	
	@CreatedTimestamp
	public Date postdate;
	
	public static Finder<Long, Message> find =
			new Finder<Long, Message>(Long.class, Message.class);
	
	@Override
	public String toString(){
		return ("[id:" + id + ", name:" + name + ", mail:" + mail +
				", message:" + message + ", date:" + postdate + "]");
	}

    /**
     * Return a page of Message
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Persona property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
	
    public static Page<Message> page(int page, int pageSize, String sortBy, String order, String filter) {
        return 
            find.where()
                .ilike("id", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }

	public static Message findById(String input){
		return Message.find.where().eq("id", input).findList().get(0);
	}
}