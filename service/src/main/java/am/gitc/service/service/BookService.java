package am.gitc.service.service;

import am.gitc.common.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Zorik Zaqaryan on 02.06.2016.
 */
@Service
public interface BookService {

    Page<Book> findAll(Pageable pageable);

    Book save(Book s, MultipartFile file, MultipartFile iamge);

    Book findOne(Long id);

    List<Book> findAll();

    List<Book> getAllPdfBook();
}
