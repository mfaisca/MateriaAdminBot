package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Leo extends _Default{
	public Leo() {
		super("Leo", "general leo", "general leo");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{7673};
				break;
			case HP:
				ids = new int[]{7674};
				break;
			case S1:
				ids = new int[]{6901};
				break;
			case S2:
				ids = new int[]{7347};
				break;
			case EX:
				ids = new int[]{7529};
				break;
			case AA:
				ids = new int[]{7358};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}