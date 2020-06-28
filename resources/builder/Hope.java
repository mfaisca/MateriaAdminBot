package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Hope extends _Default{
	public Hope() {
		super("Hope", "estheim");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{7544};
				break;
			case HP:
				ids = new int[]{7545};
				break;
			case S1:
				ids = new int[]{7534};
				break;
			case S2:
				ids = new int[]{7535};
				break;
			case EX:
				ids = new int[]{7539};
				break;
			case AA:
				ids = new int[]{7564};
				break;
			case LD:
				ids = new int[]{11115};
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