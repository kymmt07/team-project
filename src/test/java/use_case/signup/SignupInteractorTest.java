package use_case.signup;

import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class SignupInteractorTest {

    @Test
    void successTest() {
        SignupInputData inputData = new SignupInputData("user", "password");
        FakeUserDataAccess userRepository = new FakeUserDataAccess();
        final boolean[] successCalled = {false};

        // This creates a successPresenter that tests whether the test case is as we expect.
        SignupOutputBoundary successPresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // 2 things to check: the output data is correct, and the user has been created in the DAO.
                successCalled[0] = true;
                assertEquals("user", user.getUsername());
                assertTrue(userRepository.existsByName("user"));
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, successPresenter, new UserFactory());
        interactor.execute(inputData);

        assertTrue(successCalled[0], "prepareSuccessView was never called.");
    }

    @Test
    void failureUserExistsTest() {
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        // Add user to the repo so that when we check later they already exist
        UserFactory factory = new UserFactory();
        userRepository.save(factory.create("user", "pwd"));

        assertFailure(userRepository, new SignupInputData("user", "password"),
                "User already exists.");

        // The original user must not have been overwritten.
        assertEquals("pwd", userRepository.get("user").getPassword());
    }

    @Test
    void failureCommaInUsernameTest() {
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        assertFailure(userRepository, new SignupInputData("us,er", "password"),
                "Username and password cannot contain commas.");
        assertTrue(userRepository.isEmpty());
    }

    @Test
    void failureCommaInPasswordTest() {
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        assertFailure(userRepository, new SignupInputData("user", "pass,word"),
                "Username and password cannot contain commas.");
        assertTrue(userRepository.isEmpty());
    }

    @Test
    void failureEmptyPasswordTest() {
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        assertFailure(userRepository, new SignupInputData("user", ""),
                "Password cannot be empty");
        assertTrue(userRepository.isEmpty());
    }

    @Test
    void failureEmptyUsernameTest() {
        FakeUserDataAccess userRepository = new FakeUserDataAccess();

        assertFailure(userRepository, new SignupInputData("", "password"),
                "Username cannot be empty");
        assertTrue(userRepository.isEmpty());
    }

    /**
     * Runs the interactor with a presenter that expects a failure with the given
     * message, and checks that the failure view was actually reached.
     */
    private void assertFailure(SignupUserDataAccessInterface userRepository,
                               SignupInputData inputData,
                               String expectedError) {
        final boolean[] failCalled = {false};

        SignupOutputBoundary failurePresenter = new SignupOutputBoundary() {
            @Override
            public void prepareSuccessView(SignupOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                failCalled[0] = true;
                assertEquals(expectedError, error);
            }
        };

        SignupInputBoundary interactor = new SignupInteractor(userRepository, failurePresenter, new UserFactory());
        interactor.execute(inputData);

        assertTrue(failCalled[0], "prepareFailView was never called.");
    }

    /** In-memory stand-in for the real DAO, so no file is touched. */
    private static class FakeUserDataAccess implements SignupUserDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();

        @Override
        public boolean existsByName(String username) {
            return users.containsKey(username);
        }

        @Override
        public void save(User user) {
            users.put(user.getName(), user);
        }

        User get(String username) {
            return users.get(username);
        }

        boolean isEmpty() {
            return users.isEmpty();
        }
    }
}