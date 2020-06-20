#include "Login.h"
#include "MainWindow.h"

/*
 * Main.cpp
 *
 * This file includes Main function as the entry point.
 *
 * @author: Hang Yuan
 * Revised: 6/19/20
 *
 */

 /*
 * Application-Entry Page, including a Main method to raise the StarterPage.
 */
System::Void Main(array<System::String^>^ args)
{
    System::Windows::Forms::Application::EnableVisualStyles();
    System::Windows::Forms::Application::SetCompatibleTextRenderingDefault(false);

    KRSTManagementSoftware::Login^ login = gcnew KRSTManagementSoftware::Login();
    System::Windows::Forms::Application::Run(login);
}