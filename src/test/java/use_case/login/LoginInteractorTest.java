package use_case.login;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class LoginInteractorTest {

    private static final String LOGIN_ERROR = "No account with the entered username and password.";

    @Test
    void successTest() {
        LoginInputData inputData = new LoginInputData("user", "password");
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        // For the success test, we need to add user to the data access repository before we log in.
        UserFactory factory = new UserFactory();
        userRepository.add(factory.create("user", "password"));

        final boolean[] successCalled = {false};

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                successCalled[0] = true;
                assertEquals("user", user.getUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
        interactor.execute(inputData);

        assertTrue(successCalled[0], "prepareSuccessView was never called.");
    }

    @Test
    void failurePasswordMismatchTest() {
        LoginInputData inputData = new LoginInputData("user", "wrong");
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        // user exists, but the password entered does not match.
        UserFactory factory = new UserFactory();
        userRepository.add(factory.create("user", "password"));

        assertFailure(userRepository, inputData, LOGIN_ERROR);
    }

    @Test
    void failureUserDoesNotExistTest() {
        LoginInputData inputData = new LoginInputData("user", "password");

        // The repository is empty, so user does not exist.
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        assertFailure(userRepository, inputData, LOGIN_ERROR);
    }

    /**
     * Runs the interactor with a presenter that expects a failure with the given
     * message, and checks that the failure view was actually reached.
     */
    private void assertFailure(LoginUserDataAccessInterface userRepository,
                               LoginInputData inputData,
                               String expectedError) {
        final boolean[] failCalled = {false};

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                failCalled[0] = true;
                assertEquals(expectedError, error);
            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);

        assertTrue(failCalled[0], "prepareFailView was never called.");
    }

    /** In-memory stand-in for the real DAO, so no file is touched. */
    private static class FakeUserDataAccess implements LoginUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();

        void add(User user) {
            users.put(user.getName(), user);
        }

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public User get(String username) {
            return users.get(username);
        }
    }
}