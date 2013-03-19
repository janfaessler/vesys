//
//  RequstHandler.h
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#ifndef __CppSocketBankServer__RequstHandler__
#define __CppSocketBankServer__RequstHandler__

#include <iostream>
#include "Bank.h"
#include "Account.h"
#include <boost/asio.hpp>

class RequestHandler {
    Bank* bank;
    bool running = false;
    boost::asio::ip::tcp::socket *socket;
    
public:
    RequestHandler(boost::asio::ip::tcp::socket *s, Bank* b) : bank(b), socket(s) { }
    void operator()();
    
    class Request {
        string command;
        string param;
    public:
        Request(string c, string p) : command(c), param(p) {}
        string getCommand() { return command; }
        string getParam() { return param; }
    };
    
private:
    void sendCommand(string command, string param);
    Request receiveResult();
    
};

#endif /* defined(__CppSocketBankServer__RequstHandler__) */
