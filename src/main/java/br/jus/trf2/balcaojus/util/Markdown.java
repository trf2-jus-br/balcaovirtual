package br.jus.trf2.balcaojus.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.renderer.ResolvedLink;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolver;
import com.vladsch.flexmark.html2md.converter.HtmlLinkResolverFactory;
import com.vladsch.flexmark.html2md.converter.HtmlMarkdownWriter;
import com.vladsch.flexmark.html2md.converter.HtmlNodeConverterContext;
import com.vladsch.flexmark.html2md.converter.HtmlNodeRenderer;
import com.vladsch.flexmark.html2md.converter.HtmlNodeRendererFactory;
import com.vladsch.flexmark.html2md.converter.HtmlNodeRendererHandler;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;

public class Markdown {
	static class CustomLinkResolver implements HtmlLinkResolver {
		public CustomLinkResolver(HtmlNodeConverterContext context) {
		}

		@Override
		public ResolvedLink resolveLink(Node node, HtmlNodeConverterContext context, ResolvedLink link) {
			// convert all links from http:// to https://
			if (link.getUrl().startsWith("http:")) {
				return link.withUrl("https:" + link.getUrl().substring("http:".length()));
			}
			return link;
		}

		static class Factory implements HtmlLinkResolverFactory {
			@Nullable
			@Override
			public Set<Class<?>> getAfterDependents() {
				return null;
			}

			@Nullable
			@Override
			public Set<Class<?>> getBeforeDependents() {
				return null;
			}

			@Override
			public boolean affectsGlobalScope() {
				return false;
			}

			@Override
			public HtmlLinkResolver apply(HtmlNodeConverterContext context) {
				return new CustomLinkResolver(context);
			}
		}
	}

	static class HtmlConverterTextExtension implements FlexmarkHtmlConverter.HtmlConverterExtension {
		public static HtmlConverterTextExtension create() {
			return new HtmlConverterTextExtension();
		}

		@Override
		public void rendererOptions(@NotNull MutableDataHolder options) {

		}

		@Override
		public void extend(FlexmarkHtmlConverter.@NotNull Builder builder) {
			builder.linkResolverFactory(new CustomLinkResolver.Factory());
			builder.htmlNodeRendererFactory(new CustomHtmlNodeConverter.Factory());
		}
	}

	static class CustomHtmlNodeConverter implements HtmlNodeRenderer {
		public CustomHtmlNodeConverter(DataHolder options) {

		}

		@Override
		public Set<HtmlNodeRendererHandler<?>> getHtmlNodeRendererHandlers() {
			return new HashSet<>(
					Collections.singletonList(new HtmlNodeRendererHandler<>("kbd", Element.class, this::processKbd)));
		}

		private void processKbd(Element node, HtmlNodeConverterContext context, HtmlMarkdownWriter out) {
			out.append("<<");
			context.renderChildren(node, false, null);
			out.append(">>");
		}

		static class Factory implements HtmlNodeRendererFactory {
			@Override
			public HtmlNodeRenderer apply(DataHolder options) {
				return new CustomHtmlNodeConverter(options);
			}
		}
	}

	public static String convertHtmlToMarkdown(String html) {
		MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS,
				Collections.singletonList(HtmlConverterTextExtension.create()));
		String markdown = FlexmarkHtmlConverter.builder(options).build().convert(html);
		return markdown;
	}

	public static String convertMarkdownToHtml(String markdown) {
		MutableDataSet options = new MutableDataSet();

		// uncomment to set optional extensions
		// options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(),
		// StrikethroughExtension.create()));

		// uncomment to convert soft-breaks to hard breaks
		// options.set(HtmlRenderer.SOFT_BREAK, "<br />\n");

		Parser parser = Parser.builder(options).build();
		HtmlRenderer renderer = HtmlRenderer.builder(options).build();

		// You can re-use parser and renderer instances
		Document document = parser.parse(markdown);
		String html = renderer.render(document);
		return html;
	}

}