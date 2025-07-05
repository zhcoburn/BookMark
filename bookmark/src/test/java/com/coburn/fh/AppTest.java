package com.coburn.fh;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;

import com.coburn.fh.dao.User;
import com.coburn.fh.dao.UserDaoImpl;

/**
 * Unit test for simple App.
 */
public class AppTest 
{

    @Test
    public void testUserList(UserDaoImpl userDao)
    {
        List<User> users = userDao.getAll();

        for(User user : users)
            System.out.println(user);
        assertTrue(true);
    }

    @Test
    public void testUserGetById(UserDaoImpl userDao)
    {
        Optional<User> user = userDao.findById(10001);

        System.out.println(user.get());
        assertTrue(!user.isEmpty());
    }
}
