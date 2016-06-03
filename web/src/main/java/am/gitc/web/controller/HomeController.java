package am.gitc.web.controller;

import am.gitc.common.model.entity.Book;
import am.gitc.service.service.BookService;
import am.gitc.web.container.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Zorik Zaqaryan on 28.05.2016.
 */
@Controller
@RequestMapping
public class HomeController {

    private static final int BOOK_LIST_PAGE_SIZE = 10;
    private static final String BOOK_LIST_BASEURL = "/books/page/";
    private static final String SESSION_ATTRIBUTE_BOOK_LIST = "BookList";
    private static final String MODEL_ATTRIBUTE_PAGER = "pager";
    private static final String BOOK_LIST_VIEW = "book/list";
    private static final String MODEL_ATTRIBUTE_BOOKS = "books";
    @Autowired
    BookService bookService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String initHome(Model model, HttpSession session) {

        return "home";
    }

    @RequestMapping(value = "/books")
    public String booksRedirect(HttpServletRequest request) {
        request.getSession().setAttribute(SESSION_ATTRIBUTE_BOOK_LIST, null);
        return "redirect:/books/page/1";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/books/page/{pageNumber}", method = RequestMethod.GET)
    public String pagedBooksPage(HttpServletRequest request, @PathVariable Integer pageNumber, Model uiModel) {

        PagedListHolder<Book> pagedListHolder = (PagedListHolder<Book>) request.getSession()
                .getAttribute(SESSION_ATTRIBUTE_BOOK_LIST);

        if (pagedListHolder == null) {
            pagedListHolder = new PagedListHolder<>();

            pagedListHolder.setSource(bookService.findAll());
            pagedListHolder.setPageSize(BOOK_LIST_PAGE_SIZE);

        } else {

            final int goToPage = pageNumber - 1;
            if (goToPage <= pagedListHolder.getPageCount() && goToPage >= 0) {
                pagedListHolder.setPage(goToPage);
            }
        }

        request.getSession().setAttribute(SESSION_ATTRIBUTE_BOOK_LIST, pagedListHolder);

        uiModel.addAttribute(MODEL_ATTRIBUTE_PAGER, currentPage(pagedListHolder));
        uiModel.addAttribute(MODEL_ATTRIBUTE_BOOKS, pagedListHolder);

        return "home";
    }

//    @RequestMapping(value = "/products/list", method = RequestMethod.GET)
//    public String processFindForm(UserQuery userQuery, BindingResult result, Model model, HttpServletRequest request) {
//        List<Book> results = null;
//
//        Boolean isSimpleTermQuery = userQuery.getQuery().matches("[a-zA-Z_0-9 ]*");
//
//        if (StringUtils.isEmpty(userQuery.getQuery())) {
//            return "redirect:/products/search";
//        } else
//            try {
//                if (isSimpleTermQuery) {
//                    HighlightPage<Book> highlightedResults = productService
//                            .findByHighlightedNameCriteria(userQuery.getQuery());
//                    results = SolrUtils.highlightPagesToList(highlightedResults);
//                } else {
//                    results = productService.getProductsWithUserQuery(userQuery.getQuery());
//                }
//            } catch (UncategorizedSolrException ex) {
//                logger.info(MessageFormat.format("Bad Query: {0}", userQuery.getQuery()));
//                result.rejectValue("query", "product.search.error", new Object[]{userQuery.getQuery()}, "not found");
//                return PRODUCT_SEARCH_VIEW;
//            }
//
//        if (results.size() < 1) {
//            result.rejectValue("query", "product.search.noresults", new Object[]{userQuery.getQuery()}, "not found");
//            return PRODUCT_SEARCH_VIEW;
//        }
//
//        if (results.size() > 1) {
//            PagedListHolder<Product> pagedListHolder = new PagedListHolder<Product>(results);
//            pagedListHolder.setPageSize(PRODUCT_LIST_PAGE_SIZE);
//            request.getSession().setAttribute(SESSION_ATTRIBUTE_PRODUCTLIST, pagedListHolder);
//            return "redirect:/products/page/1";
//        } else {
//            Product product = results.iterator().next();
//            return "redirect:/products/" + product.getId();
//        }
//    }


    private Pager currentPage(PagedListHolder<?> pagedListHolder) {
        int currentIndex = pagedListHolder.getPage() + 1;
        int beginIndex = Math.max(1, currentIndex - BOOK_LIST_PAGE_SIZE);
        int endIndex = Math.min(beginIndex + 5, pagedListHolder.getPageCount());
        int totalPageCount = pagedListHolder.getPageCount();
        int totalItems = pagedListHolder.getNrOfElements();
        String baseUrl = BOOK_LIST_BASEURL;

        Pager pager = new Pager();
        pager.setBeginIndex(beginIndex);
        pager.setEndIndex(endIndex);
        pager.setCurrentIndex(currentIndex);
        pager.setTotalPageCount(totalPageCount);
        pager.setTotalItems(totalItems);
        pager.setBaseUrl(baseUrl);
        return pager;
    }
}
