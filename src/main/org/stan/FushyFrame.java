/*From MakingStan
 Written on 24 november 2021.
 For polymars seajam:  https://itch.io/jam/seajam
 */

package main.org.stan;

import javax.swing.*;
import java.io.IOException;

public class FushyFrame extends JFrame {

    public FushyFrame() throws IOException {
        this.add(new FushyPanel());
        this.setTitle("Fushers");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}
