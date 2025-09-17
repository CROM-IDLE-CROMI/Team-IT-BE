package ssu.cromi.teamit.repository.postboard;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.JPAExpressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ssu.cromi.teamit.entity.enums.BoardCategory;
import ssu.cromi.teamit.entity.postboard.Board;
import ssu.cromi.teamit.entity.postboard.QBoard;
import ssu.cromi.teamit.domain.QUser;
import java.util.List;

@RequiredArgsConstructor
public class BoardSearchRepositoryImpl implements BoardSearchRepository {

    private final JPAQueryFactory queryFactory;
    private final QBoard post = QBoard.board;

    @Override
    public Page<Board> searchBoards(BoardCategory category, String keyword, Pageable pageable) {
        List<Board> content = queryFactory
                .selectFrom(post)
                .where(
                        categoryEq(category),
                        searchCondition(keyword)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdAt.desc()) // 최신순
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .where(
                        categoryEq(category),
                        searchCondition(keyword)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private BooleanExpression categoryEq(BoardCategory category) {
        return category != null ? post.category.eq(category) : null;
    }

    private BooleanExpression searchCondition(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return null;
        }

        String processedKeyword = keyword.replaceAll("\\s+", "");
        QUser user = QUser.user;

        return Expressions.stringTemplate("function('REPLACE', {0}, ' ', '')", post.title)
                .containsIgnoreCase(processedKeyword)
                .or(Expressions.stringTemplate("function('REPLACE', {0}, ' ', '')", post.content)
                        .containsIgnoreCase(processedKeyword))
                .or(post.authorId.in( // 👈 authorId가 서브쿼리 결과에 포함되는지 확인
                        JPAExpressions
                                .select(user.uid)
                                .from(user)
                                .where(user.nickName.containsIgnoreCase(keyword))
                ));
    }
}