package space.luisb;

import space.luisb.dialog.ChatDialog;
import space.luisb.dialog.SetupDialog;
import space.luisb.services.ClientService;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        SetupDialog.setup();

        // Start client
        ClientService clientService = new ClientService();
        clientService.start();

        ChatDialog.start();
    }
}