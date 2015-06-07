package net.wendal.nutz.dao;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.ConnCallback;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import com.alibaba.druid.pool.DruidDataSource;

public class SimpleDaoTest {
	
	private static final Log log = Logs.get();

	@Test
	public void test_emtry() {}
	
	@Test
	public void test_insert() {
		Pet pet = new Pet("wendal", "广州", 80);
		dao.insert(pet);
		
		assertEquals(1, pet.getId());
	}

	@Test
	public void test_fastInsert() {
		List<Pet> pets = makeTestData();
		dao.fastInsert(pets);
		
		assertEquals(0, pets.get(0).getId());
	}
	
	@Test
	public void test_query() {
		dao.fastInsert(makeTestData());
		List<Pet> pets = dao.query(Pet.class, Cnd.where("age", ">", "18"));
		System.out.println(pets.size());
		
		Pager pager = dao.createPager(2, 10);
		pager.setRecordCount(dao.count(Pet.class, Cnd.where("age", ">", 30)));
		pets = dao.query(Pet.class, Cnd.where("age", ">", 30), pager);
		assertTrue(pets.size() <= 10);
		
		log.info("pager = " + pager);
	}
	
	@Test
	public void test_fetch() {
		Pet pet = dao.fetch(Pet.class);
		assertNull(pet);
		
		dao.insert(makeTestData());
		assertEquals(100, dao.count(Pet.class));
		
		pet = dao.fetch(Pet.class);
		assertNotNull(pet);
		
		pet = dao.fetch(Pet.class, 90);
		assertEquals(90, pet.getId());
		
		pet = dao.fetch(Pet.class, "w30");
		assertEquals(31, pet.getId());
		
	}
	
	@Test
	public void test_update() {
		dao.fastInsert(makeTestData());
		
		Pet pet = dao.fetch(Pet.class, 69);
		int oldAge = pet.getAge();
		pet.setAge(oldAge + 30);
		
		dao.update(pet);
		
		assertEquals(oldAge+30, dao.fetch(Pet.class, 69).getAge());
		
		dao.update(Pet.class, Chain.make("age", 18), Cnd.where("age", ">", 90));
		assertEquals(0, dao.count(Pet.class, Cnd.where("age", ">", 90)));
	}
	
	@Test
	public void test_delete() {
		dao.fastInsert(makeTestData());
		
		Pet pet = dao.fetch(Pet.class, "w49");
		dao.delete(pet);
		assertNull(dao.fetch(Pet.class, 50));
		
	}
	
	@Test
	public void test_clear() {
		dao.fastInsert(makeTestData());
		
		dao.clear(Pet.class, Cnd.where("id", "<", 18));
		assertEquals(0, dao.count(Pet.class, Cnd.where("id", "<", 18)));
	}
	
	@Test
	public void test_customSql() {
		dao.fastInsert(makeTestData());
		Sql sql = Sqls.fetchLong("select id from t_pet where nm=@name");
		sql.params().set("name", "w20");
		dao.execute(sql);
		long id = sql.getObject(Long.class);
		assertEquals(21, id);
	}
	
	@Test
	public void test_use_connection() {
		final Object[] objs = new Object[1];
		dao.run(new ConnCallback() {
			public void invoke(Connection conn) throws Exception {
				System.out.println(conn.getAutoCommit());
				objs[0] = 100;
			}
		});
		System.out.println(objs[0]);
	}
	
	public List<Pet> makeTestData() {
		List<Pet> pets = new ArrayList<Pet>(100);
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			pets.add(new Pet("w"+i, "地球", r.nextInt(130)));
		}
		assertEquals(100, pets.size());
		
		return pets;
	}
	
	public void init() {
		dao.create(Pet.class, true);
	}
	
	Ioc ioc;
	Dao dao;
	@Before
	public void before() {
		ioc = new NutIoc(new JsonLoader("ioc/dao.js"));
		dao = ioc.get(Dao.class);
		init();
	}
	
	@After
	public void after() {
		if (ioc != null)
			ioc.depose();
	}
	
	DruidDataSource ds;
//	@Before
	public void before2() {
		ds = new DruidDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl("jdbc:h2:nutz");
		
		dao = new NutDao(ds);
		
		init();
	}
	
//	@After
	public void after2() {
		if (ds != null)
			ds.close();
	}
}
