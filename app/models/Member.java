package models;

import javax.persistence.*;

import com.avaje.ebean.Page;
import com.avaje.ebean.annotation.*;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;

import java.util.*;

import play.data.validation.Constraints.*;
import play.data.validation.Constraints;

@Entity
@Table(name="members")
public class Member extends Model {
	@Id
	public Long id;

	@Required
	public String name;
	public String mail;
	public List<String> message;
	
	@Required
	public String password;
	
	@ManyToMany(cascade = CascadeType.ALL, mappedBy="members")
	public List<Message> messages = new ArrayList<Message>();

	@CreatedTimestamp
	public Date postdate;

	public static Finder<Long, Member> find =
			new Finder<Long, Member>(Long.class, Member.class);

	@Override
	public String toString(){
		return("[id:"+id+",name:"+name+",mail:"+mail+",password:"+password+"]");
	}

	public static Member findByName(String input){
		return Member.find.where().eq("name", input).findList().get(0);
	}

	public static Member findById(String input){
		return Member.find.where().eq("id", input).findList().get(0);
	}
	
    /**
     * Return a page of Member
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy Persona property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
	
    public static Page<Member> page(int page, int pageSize, String sortBy, String order, String filter) {
        return 
            find.where()
                .ilike("id", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }

    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Member c: Member.find.orderBy("name").findList()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
