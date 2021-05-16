package cryptogame.model.dao;

import cryptogame.model.dao.user.UserDao;
import cryptogame.model.models.UserModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UserDaoTest {

    @Mock
    private UserDao userDao;

    @Test
    public void testUserDaoGetUserByUsernameShouldReturnValidUser() {

        var user = new UserModel();
        user.setUsername("Test");

        Mockito.when(userDao.getByUsername("Test")).thenReturn(java.util.Optional.of(user));

        var result = userDao.getByUsername("Test");

        Assert.assertEquals("Test", result.get().getUsername());
    }

    @Test
    public void testUserDaoGetUserByUsernameShouldReturnInvalidUser() {

        var user = new UserModel();
        user.setUsername("Test");

        Mockito.when(userDao.getByUsername("Test")).thenReturn(java.util.Optional.of(user));

        var result = userDao.getByUsername("Test2");

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testUserDaoGetUserByEmailShouldReturnValidUser() {

        var user = new UserModel();
        user.setEmail("test@email.com");

        Mockito.when(userDao.getByEmail("test@email.com")).thenReturn(java.util.Optional.of(user));

        var result = userDao.getByEmail("test@email.com");

        Assert.assertEquals("test@email.com",result.get().getEmail());
    }

    @Test
    public void testUserDaoGetUserByEmailShouldReturnInvalidUser() {

        var user = new UserModel();
        user.setEmail("test@email.com");;

        Mockito.when(userDao.getByEmail("test@email.com")).thenReturn(java.util.Optional.of(user));

        var result = userDao.getByEmail("test2@email.com");

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testUserDaoGetUserByIdShouldReturnValidUser() {

        var user = new UserModel();
        user.setId(1L);

        Mockito.when(userDao.getEntity(1L)).thenReturn(java.util.Optional.of(user));

        var result = userDao.getEntity(1L).get().getId() == 1L;

        Assert.assertTrue(result);
    }

    @Test
    public void testUserDaoGetUserByIdShouldReturnInvalidUser() {

        var user = new UserModel();
        user.setId(1L);

        Mockito.when(userDao.getEntity(1L)).thenReturn(java.util.Optional.of(user));

        var result = userDao.getEntity(2L);

        Assert.assertTrue(result.isEmpty());
    }
}
