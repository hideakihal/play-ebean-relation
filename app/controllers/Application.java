package controllers;

import play.*;
import play.data.*;
import play.mvc.*;
import views.html.*;

import java.util.*;

import models.*;
import play.data.validation.Constraints.Required;

import com.avaje.ebean.Page;

public class Application extends Controller {

	public static class FindForm {
		@Required
		public String input;
	}
	
	public static Result index() {
		Page<Message> datas = Message.page(0, 10, "id", "desc", "");
		return ok(index.render("データベースのサンプル", datas));
	}

	public static Result list(Integer page, Integer size, String sortBy, String filter){
		Page<Message> datas = Message.page(page, size, sortBy, "desc", filter);
		return ok(index.render("メッセージのデータベース", datas));
	}

    public static Result add() {
    	Form<Message> f = new Form(Message.class);
    	return ok(add.render("メッセージフォーム",f));
    }

    public static Result create() {
    	Form<Message> f = new Form(Message.class).bindFromRequest();
    	if (!f.hasErrors()) {
    		Message data = f.get();
    		List<String> names = data.name;
    		for (String nm : names){
    			Member m = Member.findByName(nm);
    			data.members.add(m);
    		}
     		data.save();
    		return redirect("/");
    	} else {
    		return badRequest(add.render("ERROR",f));
    	}
    }
    
    public static Result view(Long id) {
        Form<Message> f = new Form(Message.class).fill(
        		Message.find.byId(id)
       );
       return ok(view.render("ID=" + id + "の投稿を表示。", id, f));
    }
    
    public static Result edit(Long id) {
        Form<Message> f = new Form(Message.class).fill(
        		Message.find.byId(id)
       );
       return ok(edit.render("ID=" + id + "の投稿を編集。", id, f));
    }
    
    public static Result update() {
    	Form<Message> f = new Form(Message.class).bindFromRequest();
    	if (!f.hasErrors()) {
    		Message data = f.get();
    		List<String> names = data.name;
    		for (String nm : names){
    			Member m = Member.findByName(nm);
    			data.members.add(m);
    		}
     		data.update();
    		return redirect("/");
    	} else {
    		return ok(add.render("ERROR:再度入力してください。",f));
    	}
    }
    
    public static Result delete(Long id) {
        Form<Message> f = new Form(Message.class).fill(
        		Message.find.byId(id)
       );
    	if (!f.hasErrors()) {
    		Message data = f.get();
    		if(data != null){
    			data.delete();
    			return redirect("/");
    		} else {
    			return ok(add.render("ERROR:そのID番号は見つかりません。",f));
    		}
    	} else {
    		return ok(add.render("ERROR:入力にエラーが起こりましたt。",f));
    	}
    }
    
    public static Result find(){
    	Form<FindForm> f = new Form(FindForm.class).bindFromRequest();
    	List<Message> datas = null;
    	if (!f.hasErrors()) {
    		String input = f.get().input;
    		datas = Message.find.where()
    				.like("id", "%" + input + "%").orderBy("id")
    				.findPagingList(10).getPage(0).getList();
    	}

    	return ok(find.render("投稿の検索",f,datas));
    }
}
