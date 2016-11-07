package com.clouway.http.servlets;

import com.clouway.FakeHttpServletRequest;
import com.clouway.FakeHttpServletResponse;
import com.clouway.core.Account;
import com.clouway.core.AccountRepository;
import com.clouway.core.Template;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Milev <martinmariusmilev@gmail.com>
 */
public class IndexPageServletTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  private FakeHttpServletRequest request = new FakeHttpServletRequest();
  private FakeHttpServletResponse response = new FakeHttpServletResponse();
  private ByteArrayOutputStream stream;
  private IndexPageServlet servlet;
  private PrintWriter writer;

  private AccountRepository repo = context.mock(AccountRepository.class);
  private Template template = context.mock(Template.class);

  @Before
  public void setUp() throws Exception {
    servlet = new IndexPageServlet(repo, template);
    stream = new ByteArrayOutputStream();
    writer = new PrintWriter(stream);
  }

  @Test
  public void happyPath() throws Exception {
    response.setWriter(writer);

    context.checking(new Expectations() {{
      oneOf(template).evaluate();
      will(returnValue("page"));
    }});

    servlet.doGet(request, response);
    String actual = stream.toString();
    assertThat(response.getStatus(), is(200));
    assertThat(actual, is("page"));
  }

  @Test
  public void takenUsername() throws Exception {
    String expected = "<p style=\"color:red;\">Username is taken.</p>";
    request.setParameter("name", "John");
    response.setWriter(writer);

    context.checking(new Expectations() {{
      oneOf(repo).getByName("John");
      will(returnValue(Optional.of(new Account("John","pwd",0))));
      oneOf(template).put("error", "<p style=\"color:red;\">Username is taken.</p>");
      oneOf(template).evaluate();
      will(returnValue(expected));
    }});

    servlet.doPost(request, response);
    String actual = stream.toString();
    assertThat(response.getStatus(), is(400));
    assertThat(actual, is(expected));
  }

  @Test
  public void register() throws Exception {
    request.setParameter("name", "John");
    request.setParameter("password", "Johny");
    response.setWriter(writer);

    context.checking(new Expectations() {{
      oneOf(repo).getByName("John");
      will(returnValue(Optional.empty()));
      oneOf(repo).register(new Account("John", "Johny", 0));
    }});

    servlet.doPost(request, response);
    assertThat(response.getRedirect(), is("/login"));
  }
}