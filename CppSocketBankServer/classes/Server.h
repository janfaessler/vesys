//
//  Server.h
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#ifndef __CppSocketBankServer__Server__
#define __CppSocketBankServer__Server__

#include <iostream>
#include <boost/asio.hpp>
#include "Bank.h"

using boost::asio::ip::tcp;

class Server {
    int port;
    Bank bank;

public:
    Server(int p) {
        port = p;
    }
    
    void start();
    
    class Request {
        string command;
        string param;
    public:
        Request(string c, string p) : command(c), param(p) {}
        string getCommand() { return command; }
        string getParam() { return param; }
    };
    
private:
    void handleRequest(tcp::socket* socket);
    void sendCommand(tcp::socket* socket, string command, string param);
    Request receiveResult(tcp::socket* socket);
    
};

#endif /* defined(__CppSocketBankServer__Server__) */
