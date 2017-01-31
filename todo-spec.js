/**
 * Created by Savek on 2017-01-30.
 */

describe('todo list', function() {

    var loginUsername = element(by.id('username'));
    var loginPassword = element(by.id('password'));

    function login(username, pass) {
        element(by.css('[href="#/login"]')).click();
        loginUsername.sendKeys(username);
        loginPassword.sendKeys(pass);
        element(by.css('form button.btn.btn-primary')).click();
        browser.waitForAngular();

        element(by.css('[href="/admin.html"]')).click();
    }

    function logout() {
        element(by.css('li.logout a')).click();
        browser.waitForAngular();
    }

    function addUser(username, surname, mail, login, pass, role) {
        //add User
        element(by.css('[href="#/addUser"]')).click();
        element(by.model('newUser.name')).sendKeys(username);
        element(by.model('newUser.surname')).sendKeys(surname);
        element(by.model('newUser.email')).sendKeys(mail);
        element(by.model('newUser.login')).sendKeys(login);
        element(by.model('newUser.password')).sendKeys(pass);
        element(by.css('select.hospital option:nth-child(2)')).click();
        if (role == "doctor") {
            element(by.id('2')).click();
        } else {
            element(by.id('3')).click();
        }
        element(by.model('newUser.enabled')).click();
        element(by.css('div.addUserPanel button[type="submit"]')).click();
        //add user end
    }

    function changePassword(newpass) {
        element(by.css('li.user-name a')).click();
        element(by.model('userUpdate.password')).clear();
        element(by.model('userUpdate.password')).sendKeys(newpass);
        element(by.css('div.ng-scope button[type="submit"]')).click();
    }

    beforeEach(function() {
        browser.get('http://localhost:8080/');
    });

    // przypadek dodanie usera, logowanie, edycja profilu, usuwanie
    it('add and login into user, change password, logout, login and remove', function() {
        login('admin', '123');
        addUser('protractorUserName', 'protractorUserSurName', 'protractorUser@gmail.com',
            'protractorUserLogin', '123', 'patient');
        logout();
        login('protractorUserLogin', '123');
        changePassword('1234');
        logout();
        login('protractorUserLogin', '1234');
        logout();
        login('admin', '123');
        element(by.css('[href="#/usersPanel"]')).click();
        //remove user
        element.all(by.css('td .fa-close')).last().click();
        logout();
    });

    // dodanie, usuwanie szpitala
    it('add hospital, then remove hospital', function() {
        login('admin', '123');

        element(by.css('[href="#/addHospital"]')).click();
        element(by.model('newHospital.name')).sendKeys('NewHospitalName');
        element(by.model('newHospital.adress')).sendKeys('NewHospitalAdress');
        element(by.model('newHospital.country')).sendKeys('NewHospitalCountry');
        element(by.model('newHospital.city')).sendKeys('NewHospitalCity');
        element(by.model('newHospital.passCode')).sendKeys('20-534');
        element(by.css('div.ng-scope button[type="submit"]')).click();

        element(by.css('[href="#/hospitalsPanel"]')).click();
        //expect(.getText().toContain('NewHospitalName'));
        var newHosp = element.all(by.repeater('hosp in hospitalsSliced')).last();
        expect(newHosp.element(by.css('td:nth-child(2)')).getText()).toContain('NewHospitalName');
        element.all(by.css('td .fa-close')).last().click();
        logout();
    });

    // edycja usera
    it('edit someuser', function() {
        login('admin', '123');

        element(by.css('[href="#/usersPanel"]')).click();
        var lastUser = element.all(by.repeater('user in usersSliced')).last();
        lastUser.element(by.css('td.view a')).click();

        element(by.model('userUpdate.surname')).clear();
        element(by.model('userUpdate.surname')).sendKeys("TestSurName");
        element(by.css('form[name="userDetailsForm"] button[type="submit"]')).click();

        element(by.css('[ng-click="backArrow()"]')).click();

        expect(lastUser.element(by.css('td:nth-child(2)')).getText()).toContain('TestSurName');

        logout();
    });

    //edycja konfiguracji
    it ('edit configuration', function () {
        login('admin', '123');

        element(by.css('[href="#/usersPanel"]')).click();
        var lastUser = element.all(by.repeater('user in usersSliced')).last();
        lastUser.element(by.css('td.view a')).click();

        element(by.css('li[heading="Konfiguracja"] a')).click();
        element(by.model('userConfig.measurementInterval')).clear();
        element(by.model('userConfig.measurementInterval')).sendKeys("5");

        element(by.css('form[name="configFrom"] button[type="submit"]')).click();

        logout();
    });
});

