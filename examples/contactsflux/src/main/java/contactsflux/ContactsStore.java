package contactsflux;

import eu.lestard.fluxfx.Store;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A FluxFX Store; simulates data retrieval and persistence
 *
 * @author carl
 */
public class ContactsStore extends Store {

    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    public ObservableList<Contact> getContacts() {
        return contacts;
    }

    public ContactsStore() {

        subscribe(FetchContactsAction.class,
                  (fetchContactsAction) -> {
                      Task<List<Contact>> task = new Task<List<Contact>>() {
                          @Override
                          protected List<Contact> call() throws Exception {
                              simulateTimeConsumingOp();
                              List<Contact> fromDB = new ArrayList<>();
                              fromDB.add( new Contact("Initial", "Contact"));  // test data
                              return fromDB;
                          }
                          @Override
                          protected void succeeded() {
                              super.succeeded();
                              contacts.addAll( getValue() );
                              onChange();
                          }
                      };
                      new Thread(task).start();
                  });

        subscribe(AddContactAction.class,

                  (addContactAction) -> {

                      /**
                       * Start a Task to save the data.  Upon successful completion, add the Contact to the model.
                       * Prior to starting the Task, send a reference to allow interested parties to bind JavaFX
                       * controls to the Task.
                       */

                        final String fn = addContactAction.getFirstName();
                        final String ln = addContactAction.getLastName();

                        Task<Void> task = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                updateMessage("Saving new contact");
                                updateProgress(0.5d, 1.0d);
                                simulateTimeConsumingOp();  // actually do the backend saving
                                return null;
                            }

                            @Override
                            protected void succeeded() {
                                contacts.add( new Contact(fn, ln) );  // updates the UI on fx thread
                                onChange();
                            }
                        };

                        new Thread(task).start();
                    }
        );
    }

    private void simulateTimeConsumingOp() {

        //
        // A time consuming operation to simulate data retrieval and persistence behavior
        //

        try {
            for( int i=0; i<3; i++ ) {
                HttpURLConnection c = (HttpURLConnection) new URL("http://w3c.org").openConnection();
                try (InputStream is = c.getInputStream()) {
                    int ch;
                    while ((ch = is.read()) != -1) {
                    }
                }
                c.disconnect();
            }
        } catch(Exception exc) {
            exc.printStackTrace();
        }
    }
}
