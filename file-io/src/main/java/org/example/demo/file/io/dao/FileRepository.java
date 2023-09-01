package org.example.demo.file.io.dao;

import java.util.List;

public interface FileRepository<T> {

    List<T> readFile() throws Exception;
}
