//
//  RequstHandler.cpp
//  CppSocketBankServer
//
//  Created by Shadowshine on 2/27/13.
//  Copyright (c) 2013 FHNW. All rights reserved.
//

#include "RequstHandler.h"
#include <boost/asio.hpp>

typedef unsigned long ulong;

using namespace std;
using boost::asio::ip::tcp;

#pragma mark Run Function

void RequestHandler::operator()() {
    running = true;
    cout << "RequestHandler::runing" << endl;
    
    
    while (running) {
        Request r = receiveResult();
        sendCommand("recieved", r.getCommand());

        running=false;
    }
    
    socket->close();
}


#pragma mark -
#pragma mark Connection Helper

RequestHandler::Request RequestHandler::receiveResult() {
    char buffer[500];
    socket->read_some(boost::asio::buffer(buffer));
    string msg = buffer;
    
    ulong split = msg.find(':',0);
    ulong end = msg.find(';', split);
    
    Request req(msg.substr( 0, split), msg.substr( split + 1, end - split - 1));
    cout << "Server->recv: " << req.getCommand() << ":" << req.getParam() << endl;
    return req;
}

void RequestHandler::sendCommand(string command, string param) {
    string msg = command;
    msg.append(":");
    msg.append(param);
    msg.append(";\n");
    cout << "Server->send: " << command << ":" << param << endl;
    socket->write_some(boost::asio::buffer(msg));
}