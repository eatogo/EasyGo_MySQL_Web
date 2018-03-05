package idv.ron.server.members;

import java.util.List;

public interface MemberDao {
	boolean memberExist(String user_cellphone, String password);

	boolean memberIdExist(String user_cellphone);

	int insert(Member member);

	int update(Member member);

	int delete(String user_cellphone);

	Member findById(String user_cellphone);

	List<Member> getAll();
}
