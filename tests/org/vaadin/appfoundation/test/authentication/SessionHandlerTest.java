package org.vaadin.appfoundation.test.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.appfoundation.authentication.SessionHandler;
import org.vaadin.appfoundation.authentication.data.User;
import org.vaadin.appfoundation.test.MockApplication;

public class SessionHandlerTest {

    private MockApplication application;
    private SessionHandler handler = null;

    @Before
    public void setUp() {
        // Create a new instance of the MockApplication
        application = new MockApplication();
        // Initialize the SessionHandler class with the MockApplication
        handler = new SessionHandler(application);
        handler.transactionStart(application, null);
    }

    @After
    public void tearDown() {
        handler.transactionEnd(application, null);
        handler = null;
    }

    @Test
    public void user() {
        User user = new User();
        assertNull(SessionHandler.get());

        SessionHandler.setUser(user);
        assertEquals(user, SessionHandler.get());
    }

    @Test
    public void logout() {
        User user = new User();
        SessionHandler.setUser(user);
        SessionHandler.logout();
        assertNull(SessionHandler.get());
    }
}